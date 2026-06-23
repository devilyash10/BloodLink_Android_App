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
    const val SETTINGS = "settings"
    const val ABOUT_US = "about_us"
    const val EDIT_PROFILE = "edit_profile"
    const val REQUEST_DETAIL = "request_detail"
    const val DONOR_DETAIL = "donor_detail"

    //Hospital Profile Screens
    const val INVENTORY_DASHBOARD = "inventory_dashboard"
    const val HOSPITAL_PROFILE = "hospital_profile"
    const val BLOOD_BANKS_MAP = "blood_banks_map"
    const val EMERGENCY_ALERT = "emergency_alert"


    // Helper to create donor profile route
    fun createDonorProfileRoute(donorId: String) = "donor_profile/$donorId"
}