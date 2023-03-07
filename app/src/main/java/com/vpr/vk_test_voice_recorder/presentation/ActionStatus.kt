package com.vpr.vk_test_voice_recorder.presentation

enum class ActionStatus(val errorName: String, val status: Boolean) {
    RENAME_SUCCESS("Successful rename", true),
    RENAME_FAILED("Rename failed", false),
    DELETE_SUCCESS("Successful deletion", true),
    DELETE_FAILED("Deletion failed", false),
    CREATE_SUCCESS("Successful creation", true),
    CREATE_FAILED("Creation failed", false)
}