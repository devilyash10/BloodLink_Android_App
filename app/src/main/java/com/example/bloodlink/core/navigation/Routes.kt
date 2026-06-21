package com.example.bloodlink.core.navigation

object Routes {
    // Auth Flow
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val SIGN_UP = "sign_up"

    // Main App Flow (Bottom Nav)
    const val HOME = "home"
    const val REQUESTS = "requests"
    const val CREATE_REQUEST = "create_request"
    const val NOTIFICATIONS = "notifications"
    const val PROFILE = "profile"

    // Secondary Screens
    const val SEARCH_DONORS = "search_donors"
    const val DONOR_LIST = "donor_list"
    const val DONOR_PROFILE = "donor_profile/{donorId}" // Takes an argument
    const val BLOOD_BANKS = "blood_banks"
    const val MAP_VIEW = "map_view"
    const val REQUEST_SENT = "request_sent"
    const val DONATION_HISTORY = "donation_history"

    // Helper to create donor profile route
    fun createDonorProfileRoute(donorId: String) = "donor_profile/$donorId"
}