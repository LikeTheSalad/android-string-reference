package com.likethesalad.placeholder.modules.common.helpers.dirs.utils

class ValuesNameUtils {

    companion object {
        private val VALUES_FOLDER_NAME_REGEX = Regex("values(-[a-z]{2}(-r[A-Z]{2})*)*")

        fun isValueName(name: String): Boolean {
            return VALUES_FOLDER_NAME_REGEX.matches(name)
        }

        fun getValuesNameSuffix(valuesFolderName: String): String {
            return VALUES_FOLDER_NAME_REGEX.find(valuesFolderName)!!.groupValues[1]
        }
    }
}
