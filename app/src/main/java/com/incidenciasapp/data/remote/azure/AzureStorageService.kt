package com.incidenciasapp.data.remote.azure

import android.content.Context
import android.net.Uri
import android.util.Log
import com.azure.storage.blob.BlobClient
import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobServiceClientBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class AzureStorageService(
    private val accountName: String,
    private val containerName: String,
    private val sasToken: String
) {
    private val endpoint = "https://incidenciasimg.blob.core.windows.net"

    private val blobServiceClient = BlobServiceClientBuilder()
        .endpoint(endpoint)
        .sasToken(sasToken)
        .buildClient()

    private val containerClient: BlobContainerClient =
        blobServiceClient.getBlobContainerClient(containerName)

    suspend fun uploadImage(context: Context, imageUri: Uri): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val fileName = "incidencia_${UUID.randomUUID()}.jpg"
                val blobClient: BlobClient = containerClient.getBlobClient(fileName)

                // URL SIN SAS (para acceso público)
                val blobUrl = blobClient.blobUrl

                context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                    val bytes = inputStream.readBytes()

                    try {
                        // Aquí SÍ usa el SAS internamente (viene de containerClient)
                        blobClient.upload(bytes.inputStream(), bytes.size.toLong(), true)
                    } catch (uploadException: Exception) {
                        if (uploadException.message?.contains("Wrong number of arguments") == false) {
                            throw uploadException
                        }
                    }
                }
                println("  ✓ Imagen subida: $blobUrl")
                // Devuelve URL limpia SIN SAS
                Result.success(blobUrl)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }




    suspend fun uploadMultipleImages(
        context: Context,
        imageUris: List<Uri>
    ): Result<List<String>> = withContext(Dispatchers.IO) {
        try {
            val urls = mutableListOf<String>()

            imageUris.forEachIndexed { index, uri ->
                val result = uploadImage(context, uri)

                if (result.isSuccess) {
                    urls.add(result.getOrThrow())
                } else {
                    return@withContext Result.failure(
                        Exception("Error en imagen ${index + 1}: ${result.exceptionOrNull()?.message}")
                    )
                }
            }

            Result.success(urls)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
