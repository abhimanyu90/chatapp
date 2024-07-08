package com.av.avmessenger

import android.content.Context

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@org.junit.runner.RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @org.junit.Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext: Context =
            androidx.test.platform.app.InstrumentationRegistry.getInstrumentation()
                .getTargetContext()
        org.junit.Assert.assertEquals("com.av.avmessenger", appContext.packageName)
    }
}
