package com.example.testjetpack.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.StandardCharsets

class SecurityFileUtils constructor(context: Context) {

    //双重校验锁式单例
//    companion object {
//        val instance: SecurityFileUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
//            SecurityFileUtils(this.mContext)
//        }
//    }

    var mContext: Context;

    init {
        mContext = context;
    }

    companion object {
        @Volatile
        private var instance: SecurityFileUtils? = null
        fun getInstance(context: Context): SecurityFileUtils? {
            if (instance == null) {
                synchronized(SecurityFileUtils::class) {
                    if (instance == null) {
                        instance = SecurityFileUtils(context)
                    }
                }
            }
            return instance
        }
    }

    //静态内部类式单例
//    companion object {
//        val instance = SingletonHolder.holder
//    }
//
//    private object SingletonHolder {
//        val holder = SecurityFileUtils()
//    }


    val TAG = "SecurityFileUtils";

    fun writeSecurityData(context: Context, directory: File) {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        val fileToRead = "my_sensitive_data.txt"
        val encryptedFile = EncryptedFile.Builder(
                File(directory, fileToRead),
                context,
                masterKeyAlias,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        try {
            val fileContent = "MY SUPER-SECRET INFORMATION"
                    .toByteArray(StandardCharsets.UTF_8)
            encryptedFile.openFileOutput().apply {
                write(fileContent)
                flush()
                close()
            }
        } catch (e: Exception) {
        }
    }


    fun readSecurityData(context: Context, directory: File): String {
        val key = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(key)
        val readFile = "my_sensitive_data.txt"
        val readEncrypt = EncryptedFile.Builder(
                File(directory, readFile),
                context,
                masterKeyAlias,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        )
                .build()
        lateinit var byteStream: ByteArrayOutputStream
        var fileContent: String? = null
        try {
            val fileInputSteam = readEncrypt.openFileInput()
            try {
                byteStream = ByteArrayOutputStream()
                var nextRead = fileInputSteam.read()
                while (nextRead != -1) {
                    byteStream.write(nextRead)
                    nextRead = fileInputSteam.read()
                }
                fileContent = byteStream.toString()
            } catch (e: java.lang.Exception) {
            } finally {
                fileInputSteam.close()
            }
        } catch (e: Exception) {
        }
        return fileContent!!
    }


//    lateinit var sharedPreferences: SharedPreferences;
//
//    fun getEncryptedSharedPreferences(): SharedPreferences {
//        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
//        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
//        sharedPreferences = EncryptedSharedPreferences
//                .create("Encrypted_preferences_filename", masterKeyAlias, mContext,
//                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//                )
//        return sharedPreferences;
//    }

    val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
    val sharedPreferences = EncryptedSharedPreferences
            .create("Encrypted_preferences_filename", masterKeyAlias, context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    fun getSting(key: String, default: String): String? {
        return sharedPreferences.getString(key, default)
    }

}