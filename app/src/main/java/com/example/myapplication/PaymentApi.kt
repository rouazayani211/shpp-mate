    package com.example.myapplication

    import retrofit2.Response
    import retrofit2.http.Body
    import retrofit2.http.POST
    import java.io.Serializable

    interface PaymentApi {
        /**
         * Sends a receipt request to the server.
         * @param receiptRequest The request body containing email, products, and total.
         * @return A Response object containing the API response.
         */
        @POST("/mail/send-receipt")
        suspend fun sendReceipt(@Body receiptRequest: ReceiptRequest): Response<ApiResponse>

        @POST("users/addPurchaseToHistory")
        suspend fun addPurchaseToHistory(
            @Body request: Map<String, String>
        ): Response<Unit>

    }

    // Data model for the receipt request
    data class ReceiptRequest(
        val email: String,
        val products: List<com.example.myapplication.Model.Produit>, // Update to match the actual class
        val total: Double
    )


    // Data model for the API response
    data class ApiResponse(
        val message: String,
        val success: Boolean
    )

    // Existing Produit model


    data class Produit(
        val id: String,
        val nom: String,
        val description: String,
        val prix: Double,
        val categorie: String,
        val image: String
    ) : Serializable