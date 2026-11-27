
package com.ayanet.oslauncher

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {

    private val navy = Color.rgb(16, 29, 56)
    private val gold = Color.rgb(217, 170, 69)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHome()
    }

    private fun showHome() {
        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setPadding(24, 45, 24, 25)
        root.setBackgroundColor(navy)

        val scroll = ScrollView(this)
        scroll.addView(root)

        val logo = TextView(this).apply {
            text = "A"
            textSize = 42f
            setTextColor(navy)
            gravity = Gravity.CENTER
            setBackgroundColor(gold)
        }
        root.addView(logo, LinearLayout.LayoutParams(85, 85))

        val brand = TextView(this).apply {
            text = "AYANET"
            textSize = 30f
            setTextColor(Color.WHITE)
            setPadding(0, 18, 0, 0)
        }
        root.addView(brand)

        val subtitle = TextView(this).apply {
            text = "SMART PHONE EXPERIENCE"
            textSize = 12f
            setTextColor(gold)
        }
        root.addView(subtitle)

        val clock = TextView(this).apply {
            text = SimpleDateFormat(
                "HH:mm", Locale.getDefault()
            ).format(Date())
            textSize = 48f
            setTextColor(Color.WHITE)
            setPadding(0, 40, 0, 0)
        }
        root.addView(clock)

        val date = TextView(this).apply {
            text = SimpleDateFormat(
                "EEEE, dd MMMM yyyy", Locale.getDefault()
            ).format(Date())
            textSize = 15f
            setTextColor(Color.LTGRAY)
        }
        root.addView(date)

        val welcome = TextView(this).apply {
            text = "Your world, connected."
            textSize = 19f
            setTextColor(gold)
            setPadding(0, 35, 0, 20)
        }
        root.addView(welcome)

        addAppButton(root, "📞  Phone", Intent(Intent.ACTION_DIAL))
        addAppButton(root, "📷  Camera", Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE))
        addAppButton(root, "✉️  Messages", Intent(Intent.ACTION_SENDTO).apply {
            data = android.net.Uri.parse("smsto:")
        })
        addAppButton(root, "⚙️  Phone Settings", Intent(android.provider.Settings.ACTION_SETTINGS))

        val drawer = Button(this).apply {
            text = "ALL APPLICATIONS"
            setTextColor(navy)
            setBackgroundColor(gold)
            setOnClickListener { showApps() }
        }
        root.addView(drawer, buttonParams())

        val footer = TextView(this).apply {
            text = "AYANET OS  •  VERSION 1.0"
            textSize = 11f
            gravity = Gravity.CENTER
            setTextColor(Color.LTGRAY)
            setPadding(0, 30, 0, 15)
        }
        root.addView(footer)

        setContentView(scroll)
    }

    private fun addAppButton(
        parent: LinearLayout,
        label: String,
        intent: Intent
    ) {
        val button = Button(this).apply {
            text = label
            textSize = 16f
            gravity = Gravity.CENTER_VERTICAL
            setOnClickListener {
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(
                        this@MainActivity,
                        "Application not available",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        parent.addView(button, buttonParams())
    }

    private fun buttonParams(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            65
        ).apply {
            bottomMargin = 12
        }
    }

    private fun showApps() {
        val appsIntent = Intent(Intent.ACTION_MAIN)
        appsIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val apps = packageManager.queryIntentActivities(
            appsIntent, 0
        )

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 30, 20, 20)
            setBackgroundColor(navy)
        }

        val title = TextView(this).apply {
            text = "AYANET APPLICATIONS"
            textSize = 23f
            setTextColor(gold)
            setPadding(0, 0, 0, 20)
        }
        root.addView(title)

        val back = Button(this).apply {
            text = "BACK TO HOME"
            setOnClickListener { showHome() }
        }
        root.addView(back)

        apps.sortedBy {
            it.loadLabel(packageManager).toString()
        }.forEach { app ->
            val button = Button(this).apply {
                text = app.loadLabel(packageManager).toString()
                setOnClickListener {
                    val launch = packageManager.getLaunchIntentForPackage(
                        app.activityInfo.packageName
                    )
                    if (launch != null) startActivity(launch)
                }
            }
            root.addView(button, buttonParams())
        }

        val scroll = ScrollView(this)
        scroll.addView(root)
        setContentView(scroll)
    }
}
