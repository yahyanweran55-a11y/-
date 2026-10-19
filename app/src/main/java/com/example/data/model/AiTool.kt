package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.Translate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.*

enum class ToolCategory(val titleArabic: String) {
    ALL("الكل"),
    CHAT("المحادثة"),
    WRITING("الكتابة والنصوص"),
    PROGRAMMING("البرمجة"),
    TRANSLATION("الترجمة واللغات"),
    CREATIVE("الإبداع والتصميم"),
    LEARNING("التعليم والدراسة")
}

data class AiTool(
    val id: String,
    val title: String,
    val description: String,
    val category: ToolCategory,
    val icon: ImageVector,
    val accentColor: Color,
    val placeholderPrompt: String,
    val samplePrompts: List<String>,
    val systemPromptInstructions: String
)

object AiToolsRegistry {
    val tools: List<AiTool> = listOf(
        AiTool(
            id = "ai_chat",
            title = "المساعد الذكي (شات)",
            description = "محادثة حرة ومفتوحة مع الذكاء الاصطناعي للإجابة على جميع استفساراتك",
            category = ToolCategory.CHAT,
            icon = Icons.AutoMirrored.Filled.Chat,
            accentColor = ToolChatColor,
            placeholderPrompt = "اسأل الذكاء الاصطناعي أي شيء...",
            samplePrompts = listOf(
                "ما هي أفضل النصائح لتنظيم الوقت وزيادة الإنتاجية؟",
                "اشرح لي مفهوم الحوسبة السحابية بأسلوب مبسط",
                "اقترح خطة لتعلم البرمجة من الصفر في 6 أشهر",
                "كيف يؤثر الذكاء الاصطناعي على مستقبل وظائف الغد؟"
            ),
            systemPromptInstructions = "أنت مساعد ذكاء اصطناعي عربي ودود، فطن، ومفيد جداً. أجب باللغة العربية الفصحى الواضحة والمنظمة بأسلوب ممتع ومباشر واستخدم التنسيق النقطي حيث يلزم."
        ),
        AiTool(
            id = "content_writer",
            title = "صانع المحتوى والكتابة",
            description = "كتابة مقالات، منشورات سوشيال ميديا، رسائل رسمية وقصص إبداعية",
            category = ToolCategory.WRITING,
            icon = Icons.Default.Description,
            accentColor = ToolWriterColor,
            placeholderPrompt = "اكتب الفكرة أو الموضوع، مثلاً: مقال عن فوائد القراءة، أو منشور LinkedIn...",
            samplePrompts = listOf(
                "اكتب منشوراً تسويقياً جذاباً لمنتج قهوة مختصة جديد",
                "صِغ رسالة بريد إلكتروني رسمية لطلب تدريب صيفي بشركة تقنية",
                "اكتب مقدمة مقال علمي شيق حول استكشاف كوكب المريخ",
                "اكتب قصة قصيرة مشوقة عن مغامرة في غابة سرية"
            ),
            systemPromptInstructions = "أنت كاتب محتوى ومؤلف إبداعي محترف. قم بصياغة نصوص عربية جذابة ومتقنة الصياغة، واستخدم العناوين الفرعية والفقرات المرتبة والرموز التعبيرية المناسبة إن كان النص تسويقياً أو لسوشيال ميديا."
        ),
        AiTool(
            id = "smart_translator",
            title = "المترجم الذكي المتقدم",
            description = "ترجمة دقيقة تراعي المعنى والسياق واللهجات والأمثال التعبيرية",
            category = ToolCategory.TRANSLATION,
            icon = Icons.Default.Translate,
            accentColor = ToolTranslateColor,
            placeholderPrompt = "الصق النص المراد ترجمته هنا وحدد لغة الهدف...",
            samplePrompts = listOf(
                "ترجم إلى الإنجليزية: 'النجاح ليس صدفة بل ثمرة عمل دؤوب وإصرار'",
                "Translate to Arabic: 'Consistency is what transforms average into excellence.'",
                "ترجم هذا التعبير الاصطلاحي واشرح سياقه: 'Once in a blue moon'",
                "ترجم رسالة ترحيبية مهنية من العربية إلى الفرنسية والألمانية"
            ),
            systemPromptInstructions = "أنت مترجم وخبير لغوي عالمي فوري. قدم ترجمة دقيقة، سلسة، وطبيعية غير حرفية. اذكر التفسير السياقي أو التعبيرات الاصطلاحية البديلة عند اللزوم."
        ),
        AiTool(
            id = "code_assistant",
            title = "مساعد البرمجة والأكواد",
            description = "كتابة الأكواد، كشف الأخطاء وإصلاحها، والشرح البرمجي المبسط",
            category = ToolCategory.PROGRAMMING,
            icon = Icons.Default.Code,
            accentColor = ToolCodeColor,
            placeholderPrompt = "اطلب كوداً، أو الصق كودك لاكتشاف الأخطاء وتصحيحها...",
            samplePrompts = listOf(
                "اكتب دالة بلغة Kotlin للتحقق من صحة البريد الإلكتروني مع التعليقات",
                "كيف أقرأ ملف JSON في بايثون مع التعامل مع الاستثناءات؟",
                "اشرح لي كيف تعمل Coroutines في Android بطريقة مبسطة مع مثال",
                "حوّل هذا الكود من JavaScript إلى TypeScript"
            ),
            systemPromptInstructions = "أنت مهندس برمجيات محترف وخبير في علوم الحاسوب. قدّم كوداً نظيفاً (Clean Code)، وموثقاً بتعليقات واضحة، مع شرح مختصر ودقيق لكيفية عمل الكود وحالات الخطأ الممكنة."
        ),
        AiTool(
            id = "text_summarizer",
            title = "ملخص النصوص والمقالات",
            description = "اختصار المقالات والبحوث الطويلة إلى نقاط جوهرية وأفكار رئيسية",
            category = ToolCategory.WRITING,
            icon = Icons.Default.AutoAwesome,
            accentColor = ToolSummarizeColor,
            placeholderPrompt = "الصق النص الطويل أو المقال هنا للحصول على ملخص فوري...",
            samplePrompts = listOf(
                "لخص النقاط الأساسية لأهمية النوم وتأثيره على وظائف الدماغ",
                "لخص هذا النص الطويل في 3 نقاط محددة وسريعة القراءة",
                "استخرج أهم التوصيات والقرارات من محضر الاجتماع التالي",
                "قدم ملخصاً تنفيذياً موجزاً لتقرير مالي سنوي"
            ),
            systemPromptInstructions = "أنت خبير في التحليل والتلخيص التنفيذي. استخرج الأفكار الرئيسية، والنتائج الأساسية، واعرضها في نقاط موجزة واضحة، مع خلاصة سريعة لا تتجاوز سطرين."
        ),
        AiTool(
            id = "grammar_tashkeel",
            title = "التدقيق اللغوي وتشكيل النصوص",
            description = "تصحيح الأخطاء الإملائية والنحوية وإضافة التشكيل الكامل للغة العربية",
            category = ToolCategory.TRANSLATION,
            icon = Icons.Default.Spellcheck,
            accentColor = ToolGrammarColor,
            placeholderPrompt = "أدخل النص العربي للتدقيق أو التشكيل بالحركات...",
            samplePrompts = listOf(
                "قم بتشكيل هذه الجملة بالحركات الكاملة: 'إن العلم يبني بيوتا لا عماد لها'",
                "دقق هذا النص وصحح أي أخطاء لغوية أو نحوية مع بيان سبب التصحيح",
                "أعد صياغة هذه الفقرة لتصبح بأسلوب عربي فصيح وبليغ",
                "اضبط أواخر الكلمات إعرابياً في هذه الفقرة"
            ),
            systemPromptInstructions = "أنت عالم لغوي متخصص في النحو والصرف وفقه اللغة العربية. قم بتصحيح الأخطاء النحوية والإملائية بدقة، أو شكّل النص بالحركات الصحيحة مع الحفاظ على المعنى الأصلي وتحسين الفصاحة."
        ),
        AiTool(
            id = "image_prompter",
            title = "مهندس أوامر صور الذكاء الاصطناعي",
            description = "ابتكار برومبتات احترافية لـ Midjourney و Flux بجودة سينمائية مذهلة",
            category = ToolCategory.CREATIVE,
            icon = Icons.Default.Palette,
            accentColor = ToolImagePromptColor,
            placeholderPrompt = "صف الصورة التي تتخيلها، وسنحولها إلى أوامر احترافية فائقة الدقة...",
            samplePrompts = listOf(
                "برومبت لمدينة مستقبلية بأسلوب السايبربانك تحت المطر مع إضاءة نيون",
                "برومبت بورتريه واقعي بدقة 8K لصقر عربي في الصحراء وقت الغروب",
                "تصميم شعار مينيمالي لشركة تقنية ناشئة متخصصة في الذكاء الاصطناعي",
                "مشهد كرتوني ثلاثي الأبعاد ثلاثي الأبعاد Pixar-style لروبوت صغير يقرأ كتاباً"
            ),
            systemPromptInstructions = "أنت فنان وخبير توليد صور بالذكاء الاصطناعي (AI Prompt Engineer). حوّل فكرة المستخدم إلى برومبتات إنجليزية وعربية فائقة الدقة والتفاصيل تشمل: الإضاءة (Cinematic Lighting)، النمط الفني (Style)، زاوية الكاميرا (Lens & Angle)، والجودة (8K, photorealistic) بالإضافة إلى الأبعاد ونسب العرض."
        ),
        AiTool(
            id = "quiz_generator",
            title = "صانع الاختبارات والأسئلة",
            description = "توليد اختبارات تفاعلية، بطاقات مراجعة، وأسئلة ذكية لأي تخصص",
            category = ToolCategory.LEARNING,
            icon = Icons.Default.Quiz,
            accentColor = ToolQuizColor,
            placeholderPrompt = "اكتب موضوع الاختبار وعدد الأسئلة، مثلاً: 5 أسئلة عن الجهاز الدوري...",
            samplePrompts = listOf(
                "أنشئ اختباراً من 4 أسئلة اختيار من متعدد عن تاريخ الدولة العباسية مع الإجابات",
                "أنشئ 5 أسئلة ذكية في مادة الكيمياء للمرحلة الثانوية مع توضيح الإجابة الصحيحة",
                "أنشئ بطاقات مراجعة سريعة (سؤال وجواب) لمفاهيم شبكات الحاسوب OSI Model",
                "اختبار قصير لتقييم فهم أساسيات لغة بايثون والمصفوفات"
            ),
            systemPromptInstructions = "أنت معلم وتربوي خبير. أنشئ أسئلة واضحة ومتدرجة الصعوبة بصيغة اختيار من متعدد (أ، ب، ج، د)، مع تحديد الإجابة الصحيحة في النهاية وتفسير مختصر لكل إجابة."
        ),
        AiTool(
            id = "brainstorming",
            title = "توليد الأفكار وحلول المشاكل",
            description = "عصف ذهني مبتكر لمشاريع جديدة، أسماء منتجات، واستراتيجيات تسويق",
            category = ToolCategory.CREATIVE,
            icon = Icons.Default.Lightbulb,
            accentColor = ToolIdeaColor,
            placeholderPrompt = "ما الذي تريد العصف الذهني حوله؟ مشروع، فكرة تطبيق، محتوى...",
            samplePrompts = listOf(
                "اقترح 5 أفكار مشاريع ناشئة مربحة تعتمد على الذكاء الاصطناعي في 2026",
                "اقترح 10 أسماء تجارية عربية عصرية لتطبيق توصيل طلبات سريع",
                "كيف أزيد تفاعل الجمهور على صفحتي في إنستغرام بدون إعلانات مدفوعة؟",
                "أفكار إبداعية لهدايا تخرج استثنائية وبسيطة"
            ),
            systemPromptInstructions = "أنت مستشار استراتيجي ومبتكر ريادي. قدّم أفكاراً غير تقليدية، عملية وقابلة للتنفيذ السريع، مع بيان الميزة التنافسية لكل فكرة."
        )
    )

    fun getToolById(id: String): AiTool? = tools.find { it.id == id }
}
