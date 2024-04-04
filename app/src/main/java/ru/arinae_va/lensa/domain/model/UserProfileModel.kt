package ru.arinae_va.lensa.domain.model

enum class UserProfileType {
    SPECIALIST, CUSTOMER,
}
data class UserProfileModel(
    val userId: String,
    val profileId: String,
    val type: UserProfileType,
    val name: String,
    val surname: String,
    val specialization: String,
    val rating: Float? = null,
    val avatarUrl: String? = null,
    val country: String,
    val city: String,
    val personalSite: String,
    val phoneNumber: String,
    val email: String,
    val socialMedias: List<SocialMedia>,
    val about: String,
    val portfolioUrls: List<String>? = null,
    val prices: List<Price> = emptyList(),
    val minimalPrice: Int? = null,
    val maximalPrice: Int? = null,
    val reviews: List<Review>? = null,
) {
    companion object {
        val EMPTY = UserProfileModel(
            userId = "",
            profileId = "",
            type = UserProfileType.CUSTOMER,
            name = "",
            surname = "",
            specialization = "",
            country = "",
            city = "",
            personalSite = "",
            email = "",
            socialMedias = listOf(),
            about = "",
            prices = listOf(),
            phoneNumber = ""
        )
    }
}
