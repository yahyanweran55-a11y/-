package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AiToolsRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("شامل AI", appName)
  }

  @Test
  fun `verify ai tools registry loaded`() {
    val tools = AiToolsRegistry.tools
    assertTrue(tools.isNotEmpty())
    assertTrue(tools.any { it.id == "ai_chat" })
    assertTrue(tools.any { it.id == "content_writer" })
    assertTrue(tools.any { it.id == "smart_translator" })
    assertTrue(tools.any { it.id == "code_assistant" })
  }
}
