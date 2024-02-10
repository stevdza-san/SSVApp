package com.stevdza_san.ssvapp.api

import com.google.crypto.tink.apps.rewardedads.RewardedAdsVerifier
import com.stevdza_san.ssvapp.data.MongoDB
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext

@Api(routeOverride = "verify")
suspend fun verifyReward(context: ApiContext) {
    try {
        val url = "https://website.com/path?${context.req.params.entries.joinToString("&")}"
        val userId = context.req.params["user_id"] ?: ""
        context.logger.debug("SSV URL: $url")
        context.logger.debug("SSV USERID: $userId")

        val verifier = RewardedAdsVerifier.Builder()
            .fetchVerifyingPublicKeysWith(RewardedAdsVerifier.KEYS_DOWNLOADER_INSTANCE_PROD)
            .build()
        val rewardVerified = verifier.runCatching { verify(url) }
        if (rewardVerified.isSuccess) {
            val isSuccessful = MongoDB.updateCoins(studentId = userId)
            if (isSuccessful) {
                context.logger.debug("SSV SUCCESS!")
                context.res.status = 200
            } else {
                throw IllegalArgumentException("Selected User not found.")
            }
        } else {
            context.logger.debug("SSV ERROR: ${rewardVerified.exceptionOrNull()}")
            context.res.status = 400
        }
    } catch (e: Exception) {
        context.logger.debug("SSV ERROR: $e")
        context.res.status = 400
    }
}