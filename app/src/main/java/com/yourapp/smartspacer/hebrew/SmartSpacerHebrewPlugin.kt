package com.yourapp.smartspacer.hebrew

import android.content.Context
import android.location.Location
import com.kieronquinn.app.smartspacer.sdk.model.SmartspaceTarget
import com.kieronquinn.app.smartspacer.sdk.model.uitemplatedata.Text
import com.kieronquinn.app.smartspacer.sdk.provider.SmartspacerTargetProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*

/**
 * SmartSpacer Target Provider for Hebrew Calendar
 * This displays Hebrew date and prayer times in SmartSpacer
 */
class HebrewCalendarTargetProvider : SmartspacerTargetProvider() {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private lateinit var repository: HebrewCalendarRepository

    override fun onCreate() {
        super.onCreate()
        repository = HebrewCalendarRepository(context!!)
    }

    override fun getSmartspaceTargets(targetId: String): List<SmartspaceTarget> {
        val targets = mutableListOf<SmartspaceTarget>()

        scope.launch {
            try {
                val location = getCurrentLocation()

                val hebdateResult = repository.getHebdate()
                val zmanResult = repository.getZman(location)

                if (hebdateResult.isSuccess && zmanResult.isSuccess) {
                    val hebdate = hebdateResult.getOrNull()
                    val zman = zmanResult.getOrNull()

                    val target = SmartspaceTarget(
                        smartspaceTargetId = targetId,
                        headerAction = SmartspaceTarget.TapAction(
                            id = "hebrew_calendar_tap",
                            intent = null
                        ),
                        baseAction = SmartspaceTarget.TapAction(
                            id = "hebrew_calendar_base_tap"
                        ),
                        featureType = SmartspaceTarget.FEATURE_CALENDAR,
                        iconRes = android.R.drawable.ic_menu_today,
                        title = Text(hebdate?.hebdate ?: ""),
                        subtitle = Text(zman?.zman ?: "")
                    )

                    targets.add(target)
                    notifyChange()
                }
            } catch (e: Exception) {
                // ignore for now
            }
        }

        return targets
    }

    override fun onDismiss(smartspaceTargetId: String, targetId: String): Boolean {
        return true
    }

    private fun getCurrentLocation(): Location? {
        // TODO: implement real location
        return null
    }

    fun scheduleUpdates(context: Context) {
        // TODO: implement with WorkManager
    }
}

/**
 * Complication Provider for %hebdate placeholder
 */
class HebdateComplicationProvider : SmartspacerTargetProvider() {

    private lateinit var repository: HebrewCalendarRepository

    override fun onCreate() {
        super.onCreate()
        repository = HebrewCalendarRepository(context!!)
    }

    override fun getSmartspaceTargets(targetId: String): List<SmartspaceTarget> {
        return emptyList()
    }

    override fun onDismiss(smartspaceTargetId: String, targetId: String): Boolean {
        return false
    }
}

/**
 * Complication Provider for %zman placeholder
 */
class ZmanComplicationProvider : SmartspacerTargetProvider() {

    private lateinit var repository: HebrewCalendarRepository

    override fun onCreate() {
        super.onCreate()
        repository = HebrewCalendarRepository(context!!)
    }

    override fun getSmartspaceTargets(targetId: String): List<SmartspaceTarget> {
        return emptyList()
    }

    override fun onDismiss(smartspaceTargetId: String, targetId: String): Boolean {
        return false
    }
}
