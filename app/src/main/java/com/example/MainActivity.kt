package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.MyApplicationTheme
import androidx.compose.ui.draw.drawBehind

class MainActivity : ComponentActivity() {
    private val viewModel: SleepSchedulerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SleepSchedulerApp(viewModel = viewModel)
            }
        }
    }
}

class Localization(val language: Language) {
    val appTitle = if (language == Language.PERSIAN) "چرخه خواب" else "Sleep Cycles"
    val appSubtitle = if (language == Language.PERSIAN) "بیدار شدن با نشاط کامل براساس محاسبات خواب عمیق" else "Wake up energized with science-backed cycles"
    val wakeUpTab = if (language == Language.PERSIAN) "ساعت بیداری" else "Wake-up Time"
    val sleepTab = if (language == Language.PERSIAN) "ساعت خوابیدن" else "Bedtime"
    val chooseWakeTime = if (language == Language.PERSIAN) "ساعت بیدار شدن مورد نظر خود را وارد کنید:" else "Set your target wake-up time:"
    val chooseSleepTime = if (language == Language.PERSIAN) "ساعت به خواب رفتن مورد نظر خود را وارد کنید:" else "Set your target bedtime:"
    val hours = if (language == Language.PERSIAN) "ساعت" else "Hours"
    val minutes = if (language == Language.PERSIAN) "دقیقه" else "Minutes"
    val sleepNowBtn = if (language == Language.PERSIAN) "الان می‌خوابم (محاسبه زمان بیداری)" else "Sleep Now (Immediate)"
    val fallAsleepTitle = if (language == Language.PERSIAN) "مدت زمان متوسط برای به خواب رفتن" else "Average time to fall asleep"
    val fallAsleepUnit = if (language == Language.PERSIAN) "%d دقیقه" else "%d minutes"
    val recommendedTimes = if (language == Language.PERSIAN) "بهترین زمان‌های پیشنهادی:" else "Recommended schedules:"
    val bedtimeResultLabel = if (language == Language.PERSIAN) "ساعت مناسب خوابیدن" else "Go to sleep at"
    val wakeUpResultLabel = if (language == Language.PERSIAN) "ساعت مناسب بیدار شدن" else "Wake up at"
    val cycleLabel = if (language == Language.PERSIAN) "%d چرخه (%s ساعت خواب)" else "%d cycles (%s hrs sleep)"
    val recommendedBadge = if (language == Language.PERSIAN) "پیشنهاد عالی" else "Optimal"
    val minimumBadge = if (language == Language.PERSIAN) "حداقل خواب" else "Minimum"
    val goodBadge = if (language == Language.PERSIAN) "خوب" else "Good"
    val sleepTipsTitle = if (language == Language.PERSIAN) "چرا چرخه خواب ۹۰ دقیقه‌ای مهم است؟" else "Why is the 90-minute cycle key?"
    val tip1 = if (language == Language.PERSIAN) "⏰ ساختار خواب: خواب انسان شامل چرخه‌های متوالی ۹۰ دقیقه‌ای است. بیدار شدن در پایان یک چرخه کامل مانع از خستگی و کسالت می‌شود." else "⏰ Sleep Structure: Human sleep consists of consecutive 90-minute cycles. Waking up at the end of a full cycle prevents fatigue and grogginess."
    val tip2 = if (language == Language.PERSIAN) "📱 نور آبی مضر: حداقل یک ساعت قبل از خواب از کار با گوشی، تبلت و مانیتور خودداری کنید تا ملاتونین کافی ترشح شود." else "📱 Blue Light: Avoid smartphones, tablets, and monitors for at least one hour before sleeping to stimulate melatonin production."
    val tip3 = if (language == Language.PERSIAN) "☕ کافئین و تغذیه: از مصرف چای، قهوه و نوشیدنی‌های انرژی‌زا یا غذاهای بسیار سنگین ۴ تا ۶ ساعت پیش از خواب دوری کنید." else "☕ Caffeine & Diet: Steer clear of energy drinks, coffee, tea, or heavy meals 4 to 6 hours before bedtime."
    val tip4 = if (language == Language.PERSIAN) "🌙 دمای اتاق خواب: دمای خنک (حدود ۱۸ تا ۲۰ درجه سانتی‌گراد)، محیط کاملاً تاریک و بی‌صدا کیفیت خواب عمیق را دوچندان می‌کند." else "🌙 Environment: A cool bedroom (around 18-20°C), dark space, and absolute quiet double your deep sleep quality."
    val developerNote = if (language == Language.PERSIAN) "طراحی شده برای ارتقای سلامت خواب و نشاط صبحگاهی" else "Designed to improve sleep hygiene and morning energy"

    val enterHourTitle = if (language == Language.PERSIAN) "وارد کردن ساعت" else "Enter Hour"
    val enterHourLabel = if (language == Language.PERSIAN) "ساعت مورد نظر خود را وارد کنید (۰ تا ۲۳):" else "Enter your target hour (0 to 23):"
    val enterMinuteTitle = if (language == Language.PERSIAN) "وارد کردن دقیقه" else "Enter Minute"
    val enterMinuteLabel = if (language == Language.PERSIAN) "دقیقه مورد نظر خود را وارد کنید (۰ تا ۵۹):" else "Enter your target minute (0 to 59):"
    val enterFallAsleepTitle = if (language == Language.PERSIAN) "مدت زمان به خواب رفتن" else "Fall Asleep Duration"
    val enterFallAsleepLabel = if (language == Language.PERSIAN) "مدت زمان متوسط را وارد کنید (۰ تا ۴۵ دقیقه):" else "Enter average duration (0 to 45 mins):"
    val confirmBtn = if (language == Language.PERSIAN) "تایید" else "Confirm"
    val cancelBtn = if (language == Language.PERSIAN) "لغو" else "Cancel"
}

@Composable
fun SleepSchedulerApp(viewModel: SleepSchedulerViewModel) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val isWakeUpMode by viewModel.isWakeUpMode.collectAsStateWithLifecycle()
    val hour by viewModel.hour.collectAsStateWithLifecycle()
    val minute by viewModel.minute.collectAsStateWithLifecycle()
    val fallAsleepMinutes by viewModel.fallAsleepMinutes.collectAsStateWithLifecycle()
    val sleepOptions by viewModel.sleepOptions.collectAsStateWithLifecycle()

    val loc = remember(language) { Localization(language) }

    var showHourDialog by remember { mutableStateOf(false) }
    var hourInput by remember { mutableStateOf("") }

    var showMinuteDialog by remember { mutableStateOf(false) }
    var minuteInput by remember { mutableStateOf("") }

    var showFallAsleepDialog by remember { mutableStateOf(false) }
    var fallAsleepInput by remember { mutableStateOf("") }

    if (showHourDialog) {
        AlertDialog(
            onDismissRequest = { showHourDialog = false },
            title = { Text(text = loc.enterHourTitle, color = Color(0xFFF4EFE6), fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(text = loc.enterHourLabel, color = Color(0xFFA39CB8), fontSize = 13.sp, modifier = Modifier.padding(bottom = 8.dp))
                    OutlinedTextField(
                        value = hourInput,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() } && input.length <= 2) {
                                hourInput = input
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFFF4EFE6),
                            unfocusedTextColor = Color(0xFFF4EFE6),
                            focusedBorderColor = Color(0xFFE2A640),
                            unfocusedBorderColor = Color(0xFFA39CB8).copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("hour_input_field")
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val parsed = hourInput.toIntOrNull() ?: 0
                        val coerced = parsed.coerceIn(0, 23)
                        viewModel.updateHour(coerced)
                        showHourDialog = false
                    },
                    modifier = Modifier.testTag("hour_dialog_confirm")
                ) {
                    Text(text = loc.confirmBtn, color = Color(0xFFE2A640), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showHourDialog = false },
                    modifier = Modifier.testTag("hour_dialog_cancel")
                ) {
                    Text(text = loc.cancelBtn, color = Color(0xFFA39CB8))
                }
            },
            containerColor = Color(0xFF14121F),
            shape = RoundedCornerShape(22.dp)
        )
    }

    if (showMinuteDialog) {
        AlertDialog(
            onDismissRequest = { showMinuteDialog = false },
            title = { Text(text = loc.enterMinuteTitle, color = Color(0xFFF4EFE6), fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(text = loc.enterMinuteLabel, color = Color(0xFFA39CB8), fontSize = 13.sp, modifier = Modifier.padding(bottom = 8.dp))
                    OutlinedTextField(
                        value = minuteInput,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() } && input.length <= 2) {
                                minuteInput = input
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFFF4EFE6),
                            unfocusedTextColor = Color(0xFFF4EFE6),
                            focusedBorderColor = Color(0xFFE2A640),
                            unfocusedBorderColor = Color(0xFFA39CB8).copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("minute_input_field")
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val parsed = minuteInput.toIntOrNull() ?: 0
                        val coerced = parsed.coerceIn(0, 59)
                        viewModel.updateMinute(coerced)
                        showMinuteDialog = false
                    },
                    modifier = Modifier.testTag("minute_dialog_confirm")
                ) {
                    Text(text = loc.confirmBtn, color = Color(0xFFE2A640), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showMinuteDialog = false },
                    modifier = Modifier.testTag("minute_dialog_cancel")
                ) {
                    Text(text = loc.cancelBtn, color = Color(0xFFA39CB8))
                }
            },
            containerColor = Color(0xFF14121F),
            shape = RoundedCornerShape(22.dp)
        )
    }

    if (showFallAsleepDialog) {
        AlertDialog(
            onDismissRequest = { showFallAsleepDialog = false },
            title = { Text(text = loc.enterFallAsleepTitle, color = Color(0xFFF4EFE6), fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(text = loc.enterFallAsleepLabel, color = Color(0xFFA39CB8), fontSize = 13.sp, modifier = Modifier.padding(bottom = 8.dp))
                    OutlinedTextField(
                        value = fallAsleepInput,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() } && input.length <= 2) {
                                fallAsleepInput = input
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFFF4EFE6),
                            unfocusedTextColor = Color(0xFFF4EFE6),
                            focusedBorderColor = Color(0xFFE2A640),
                            unfocusedBorderColor = Color(0xFFA39CB8).copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("fall_asleep_input_field")
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val parsed = fallAsleepInput.toIntOrNull() ?: 0
                        val coerced = parsed.coerceIn(0, 45)
                        viewModel.updateFallAsleepMinutes(coerced)
                        showFallAsleepDialog = false
                    },
                    modifier = Modifier.testTag("fall_asleep_dialog_confirm")
                ) {
                    Text(text = loc.confirmBtn, color = Color(0xFFE2A640), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showFallAsleepDialog = false },
                    modifier = Modifier.testTag("fall_asleep_dialog_cancel")
                ) {
                    Text(text = loc.cancelBtn, color = Color(0xFFA39CB8))
                }
            },
            containerColor = Color(0xFF14121F),
            shape = RoundedCornerShape(22.dp)
        )
    }

    // Dynamic RTL/LTR layout direction swap based on Persian/English selection
    val layoutDirection = if (language == Language.PERSIAN) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0B0A14)) // Deep Cosmic Dark Background
                    .drawBehind {
                        // Ambient Gold/Yellow Glow (top-right)
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFFE2A640).copy(alpha = 0.12f), Color.Transparent),
                                center = androidx.compose.ui.geometry.Offset(size.width * 0.9f, 0f),
                                radius = size.width * 0.8f
                            ),
                            radius = size.width * 0.8f,
                            center = androidx.compose.ui.geometry.Offset(size.width * 0.9f, 0f)
                        )
                        // Ambient Teal Glow (bottom-left)
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFF2DD4BF).copy(alpha = 0.10f), Color.Transparent),
                                center = androidx.compose.ui.geometry.Offset(0f, size.height),
                                radius = size.width * 0.9f
                            ),
                            radius = size.width * 0.9f,
                            center = androidx.compose.ui.geometry.Offset(0f, size.height)
                        )
                        // Subtle starry dots
                        val starColor = Color(0xFFF4EFE6).copy(alpha = 0.3f)
                        val starPoints = listOf(
                            0.15f to 0.12f,
                            0.85f to 0.08f,
                            0.45f to 0.22f,
                            0.78f to 0.35f,
                            0.25f to 0.48f,
                            0.62f to 0.58f,
                            0.18f to 0.72f,
                            0.88f to 0.78f,
                            0.38f to 0.85f,
                            0.70f to 0.92f
                        )
                        starPoints.forEach { (xPercent, yPercent) ->
                            drawCircle(
                                color = starColor,
                                radius = 1.5.dp.toPx(),
                                center = androidx.compose.ui.geometry.Offset(size.width * xPercent, size.height * yPercent)
                            )
                        }
                    }
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
                ) {
                    // Header Area
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(Color(0xFFE2A640), Color(0xFFC2876B))
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .border(
                                            1.dp,
                                            Color(0xFFF4EFE6).copy(alpha = 0.25f),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bedtime,
                                        contentDescription = "App Icon",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = loc.appTitle,
                                        color = Color(0xFFF4EFE6),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = loc.appSubtitle,
                                        color = Color(0xFFA39CB8),
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            // Dynamic Language Toggle Button
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFF4EFE6).copy(alpha = 0.04f), shape = RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFFE2A640).copy(alpha = 0.14f), shape = RoundedCornerShape(12.dp))
                                    .clickable {
                                        viewModel.setLanguage(
                                            if (language == Language.PERSIAN) Language.ENGLISH else Language.PERSIAN
                                        )
                                    }
                                    .testTag("language_toggle_button")
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = if (language == Language.PERSIAN) "🇺🇸 EN" else "🇮🇷 FA",
                                    color = Color(0xFFF4EFE6),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Mode Segmented Control Tabs
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF14121F), shape = RoundedCornerShape(16.dp))
                                .border(1.dp, Color(0xFFE2A640).copy(alpha = 0.14f), shape = RoundedCornerShape(16.dp))
                                .padding(4.dp)
                        ) {
                            // Target Wake Up Mode
                            val isWakeUpSelected = isWakeUpMode
                            val wakeUpBg = if (isWakeUpSelected) Color(0xFFE2A640).copy(alpha = 0.12f) else Color.Transparent
                            val wakeUpBorder = if (isWakeUpSelected) BorderStroke(1.dp, Color(0xFFE2A640).copy(alpha = 0.4f)) else null
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(wakeUpBg, shape = RoundedCornerShape(12.dp))
                                    .then(if (wakeUpBorder != null) Modifier.border(wakeUpBorder.width, wakeUpBorder.brush, shape = RoundedCornerShape(12.dp)) else Modifier)
                                    .clickable { viewModel.setWakeUpMode(true) }
                                    .testTag("wake_up_tab")
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.WbSunny,
                                        contentDescription = null,
                                        tint = if (isWakeUpSelected) Color(0xFFE2A640) else Color(0xFFA39CB8),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = loc.wakeUpTab,
                                        color = if (isWakeUpSelected) Color.White else Color(0xFFA39CB8),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Target Bedtime Mode
                            val isSleepSelected = !isWakeUpMode
                            val sleepBg = if (isSleepSelected) Color(0xFF2DD4BF).copy(alpha = 0.12f) else Color.Transparent
                            val sleepBorder = if (isSleepSelected) BorderStroke(1.dp, Color(0xFF2DD4BF).copy(alpha = 0.4f)) else null
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(sleepBg, shape = RoundedCornerShape(12.dp))
                                    .then(if (sleepBorder != null) Modifier.border(sleepBorder.width, sleepBorder.brush, shape = RoundedCornerShape(12.dp)) else Modifier)
                                    .clickable { viewModel.setWakeUpMode(false) }
                                    .testTag("sleep_tab")
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Bedtime,
                                        contentDescription = null,
                                        tint = if (isSleepSelected) Color(0xFF2DD4BF) else Color(0xFFA39CB8),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = loc.sleepTab,
                                        color = if (isSleepSelected) Color.White else Color(0xFFA39CB8),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Main Interactive Time Selection Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF14121F).copy(alpha = 0.85f)),
                            border = BorderStroke(1.dp, Color(0xFFE2A640).copy(alpha = 0.14f)),
                            shape = RoundedCornerShape(22.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (isWakeUpMode) loc.chooseWakeTime else loc.chooseSleepTime,
                                    color = Color(0xFFA39CB8),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                // Gorgeous Large Tactile Time Controller Widget
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    // Hour Selector Column
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        IconButton(
                                            onClick = { viewModel.updateHour(hour + 1) },
                                            modifier = Modifier.testTag("hour_up_button")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowUp,
                                                contentDescription = "Increase Hour",
                                                tint = Color(0xFFE2A640)
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFF0B0A14).copy(alpha = 0.6f), shape = RoundedCornerShape(8.dp))
                                                .border(1.dp, Color(0xFFE2A640).copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp))
                                                .clickable {
                                                    hourInput = hour.toString()
                                                    showHourDialog = true
                                                }
                                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                                .testTag("hour_text_click")
                                        ) {
                                            Text(
                                                text = String.format("%02d", hour),
                                                style = TextStyle(
                                                    fontFamily = FontFamily.Monospace,
                                                    fontSize = 48.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFFF4EFE6)
                                                )
                                            )
                                        }
                                        IconButton(
                                            onClick = { viewModel.updateHour(hour - 1) },
                                            modifier = Modifier.testTag("hour_down_button")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowDown,
                                                contentDescription = "Decrease Hour",
                                                tint = Color(0xFFE2A640)
                                            )
                                        }
                                        Text(
                                            text = loc.hours,
                                            color = Color(0xFFA39CB8),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    Text(
                                        text = ":",
                                        style = TextStyle(
                                            fontSize = 44.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFE2A640).copy(alpha = 0.6f)
                                        ),
                                        modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 22.dp)
                                    )

                                    // Minute Selector Column
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        IconButton(
                                            onClick = { viewModel.updateMinute(minute + 5) },
                                            modifier = Modifier.testTag("minute_up_button")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowUp,
                                                contentDescription = "Increase Minute by 5",
                                                tint = Color(0xFFE2A640)
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFF0B0A14).copy(alpha = 0.6f), shape = RoundedCornerShape(8.dp))
                                                .border(1.dp, Color(0xFFE2A640).copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp))
                                                .clickable {
                                                    minuteInput = minute.toString()
                                                    showMinuteDialog = true
                                                }
                                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                                .testTag("minute_text_click")
                                        ) {
                                            Text(
                                                text = String.format("%02d", minute),
                                                style = TextStyle(
                                                    fontFamily = FontFamily.Monospace,
                                                    fontSize = 48.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFFF4EFE6)
                                                )
                                            )
                                        }
                                        IconButton(
                                            onClick = { viewModel.updateMinute(minute - 5) },
                                            modifier = Modifier.testTag("minute_down_button")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowDown,
                                                contentDescription = "Decrease Minute by 5",
                                                tint = Color(0xFFE2A640)
                                            )
                                        }
                                        Text(
                                            text = loc.minutes,
                                            color = Color(0xFFA39CB8),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Dynamic Sleep Now Quick Button
                                Button(
                                    onClick = { viewModel.sleepNow() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(Color(0xFFE2A640), Color(0xFFC2876B))
                                            ),
                                            shape = RoundedCornerShape(14.dp)
                                        )
                                        .testTag("sleep_now_button"),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent
                                    ),
                                    shape = RoundedCornerShape(14.dp),
                                    contentPadding = PaddingValues(vertical = 12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.NightsStay,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = loc.sleepNowBtn,
                                            color = Color.White,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Fall Asleep Duration Custom Offset
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF14121F).copy(alpha = 0.85f)),
                            border = BorderStroke(1.dp, Color(0xFFE2A640).copy(alpha = 0.14f)),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = loc.fallAsleepTitle,
                                        color = Color(0xFFF4EFE6),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .background(Color(0xFF0B0A14).copy(alpha = 0.6f), shape = RoundedCornerShape(8.dp))
                                            .border(1.dp, Color(0xFFE2A640).copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp))
                                            .clickable {
                                                fallAsleepInput = fallAsleepMinutes.toString()
                                                showFallAsleepDialog = true
                                            }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                            .testTag("fall_asleep_text_click")
                                    ) {
                                        Text(
                                            text = String.format(loc.fallAsleepUnit, fallAsleepMinutes),
                                            color = Color(0xFFE2A640),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Duration",
                                            tint = Color(0xFFE2A640).copy(alpha = 0.8f),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                                Slider(
                                    value = fallAsleepMinutes.toFloat(),
                                    onValueChange = { viewModel.updateFallAsleepMinutes(it.toInt()) },
                                    valueRange = 0f..45f,
                                    steps = 44, // 1-minute steps
                                    colors = SliderDefaults.colors(
                                        thumbColor = Color(0xFFE2A640),
                                        activeTrackColor = Color(0xFFE2A640),
                                        inactiveTrackColor = Color(0xFFA39CB8).copy(alpha = 0.2f)
                                    ),
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .testTag("fall_asleep_slider")
                                )
                            }
                        }
                    }

                    // Section Divider & Recommended Label
                    item {
                        Text(
                            text = loc.recommendedTimes,
                            color = Color(0xFFF4EFE6),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    // Dynamic Recommendations List
                    items(sleepOptions) { option ->
                        val formattedTime = option.time.format()

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("recommendation_card_${option.cycles}"),
                            colors = CardDefaults.cardColors(
                                containerColor = if (option.isOptimal) Color(0xFFE2A640).copy(alpha = 0.12f) else Color(0xFF14121F).copy(alpha = 0.85f)
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (option.isOptimal) Color(0xFFE2A640).copy(alpha = 0.5f) else Color(0xFFE2A640).copy(alpha = 0.14f)
                            ),
                            shape = RoundedCornerShape(18.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = if (isWakeUpMode) loc.bedtimeResultLabel else loc.wakeUpResultLabel,
                                        color = Color(0xFFA39CB8),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = formattedTime,
                                        style = TextStyle(
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (option.isOptimal) Color(0xFFE2A640) else Color(0xFFF4EFE6)
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = String.format(
                                            loc.cycleLabel,
                                            option.cycles,
                                            option.durationHours.toString()
                                        ),
                                        color = Color(0xFFA39CB8),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                // Interactive State Badging & Visualizing Dots
                                Column(horizontalAlignment = Alignment.End) {
                                    val badgeText = when (option.cycles) {
                                        6, 5 -> loc.recommendedBadge
                                        4 -> loc.goodBadge
                                        else -> loc.minimumBadge
                                    }
                                    val badgeColor = when (option.cycles) {
                                        6, 5 -> Color(0xFF2DD4BF) // Emerald/Teal
                                        4 -> Color(0xFFE2A640)    // Gold
                                        else -> Color(0xFFC2876B)  // Copper/Orange
                                    }

                                    Box(
                                        modifier = Modifier
                                            .background(
                                                badgeColor.copy(alpha = 0.15f),
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .border(
                                                1.dp,
                                                badgeColor.copy(alpha = 0.4f),
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = badgeText,
                                            color = badgeColor,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Display dots visualizing sleep cycles
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        repeat(option.cycles) {
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .background(
                                                        color = if (option.isOptimal) Color(0xFFE2A640) else Color(0xFFA39CB8).copy(alpha = 0.5f),
                                                        shape = CircleShape
                                                    )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Collapsible "Key Sleep Science Tips" Panel
                    item {
                        var tipsExpanded by remember { mutableStateOf(false) }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { tipsExpanded = !tipsExpanded }
                                .testTag("tips_card"),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF14121F).copy(alpha = 0.85f)),
                            border = BorderStroke(1.dp, Color(0xFFE2A640).copy(alpha = 0.14f)),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = null,
                                            tint = Color(0xFFE2A640),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = loc.sleepTipsTitle,
                                            color = Color(0xFFF4EFE6),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Icon(
                                        imageVector = if (tipsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Expand details",
                                        tint = Color(0xFFA39CB8),
                                        modifier = Modifier
                                            .size(24.dp)
                                            .testTag("tips_expand_button")
                                    )
                                }

                                if (tipsExpanded) {
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Text(
                                            text = loc.tip1,
                                            color = Color(0xFFA39CB8),
                                            fontSize = 13.sp,
                                            lineHeight = 18.sp
                                        )
                                        Text(
                                            text = loc.tip2,
                                            color = Color(0xFFA39CB8),
                                            fontSize = 13.sp,
                                            lineHeight = 18.sp
                                        )
                                        Text(
                                            text = loc.tip3,
                                            color = Color(0xFFA39CB8),
                                            fontSize = 13.sp,
                                            lineHeight = 18.sp
                                        )
                                        Text(
                                            text = loc.tip4,
                                            color = Color(0xFFA39CB8),
                                            fontSize = 13.sp,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Footer signature
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = loc.developerNote,
                                color = Color(0xFFA39CB8).copy(alpha = 0.5f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
