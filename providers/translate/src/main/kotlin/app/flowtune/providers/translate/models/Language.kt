package app.flowtune.providers.translate.models

import kotlin.enums.enumEntries

enum class Language(val code: String) {
    Auto(code = "auto"),

    Afrikaans(code = "af"),
    Albanian(code = "sq"),
    Amharic(code = "am"),
    Arabic(code = "ar"),
    Armenian(code = "hy"),
    Azerbaijani(code = "az"),
    Basque(code = "eu"),
    Belarusian(code = "be"),
    Bengali(code = "bn"),
    Bosnian(code = "bs"),
    Bulgarian(code = "bg"),
    Catalan(code = "ca"),
    Cebuano(code = "ceb"),
    Chichewa(code = "ny"),
    ChineseSimplified(code = "zh-cn"),
    ChineseTraditional(code = "zh-tw"),
    Corsican(code = "co"),
    Croatian(code = "hr"),
    Czech(code = "cs"),
    Danish(code = "da"),
    Dutch(code = "nl"),
    English(code = "en"),
    Esperanto(code = "eo"),
    Estonian(code = "et"),
    Filipino(code = "tl"),
    Finnish(code = "fi"),
    French(code = "fr"),
    Frisian(code = "fy"),
    Galician(code = "gl"),
    Georgian(code = "ka"),
    German(code = "de"),
    Greek(code = "el"),
    Gujarati(code = "gu"),
    HaitianCreole(code = "ht"),
    Hausa(code = "ha"),
    Hawaiian(code = "haw"),
    Hebrew1(code = "iw"),
    Hebrew2(code = "he"),
    Hindi(code = "hi"),
    Hmong(code = "hmn"),
    Hungarian(code = "hu"),
    Icelandic(code = "is"),
    Igbo(code = "ig"),
    Indonesian(code = "id"),
    Irish(code = "ga"),
    Italian(code = "it"),
    Japanese(code = "ja"),
    Javanese(code = "jw"),
    Kannada(code = "kn"),
    Kazakh(code = "kk"),
    Khmer(code = "km"),
    Korean(code = "ko"),
    KurdishKurmanji(code = "ku"),
    Kyrgyz(code = "ky"),
    Lao(code = "lo"),
    Latin(code = "la"),
    Latvian(code = "lv"),
    Lithuanian(code = "lt"),
    Luxembourgish(code = "lb"),
    Macedonian(code = "mk"),
    Malagasy(code = "mg"),
    Malay(code = "ms"),
    Malayalam(code = "ml"),
    Maltese(code = "mt"),
    Maori(code = "mi"),
    Marathi(code = "mr"),
    Mongolian(code = "mn"),
    MyanmarBurmese(code = "my"),
    Nepali(code = "ne"),
    Norwegian(code = "no"),
    Odia(code = "or"),
    Pashto(code = "ps"),
    Persian(code = "fa"),
    Polish(code = "pl"),
    Portuguese(code = "pt"),
    Punjabi(code = "pa"),
    Romanian(code = "ro"),
    Russian(code = "ru"),
    Samoan(code = "sm"),
    ScotsGaelic(code = "gd"),
    Serbian(code = "sr"),
    Sesotho(code = "st"),
    Shona(code = "sn"),
    Sindhi(code = "sd"),
    Sinhala(code = "si"),
    Slovak(code = "sk"),
    Slovenian(code = "sl"),
    Somali(code = "so"),
    Spanish(code = "es"),
    Sundanese(code = "su"),
    Swahili(code = "sw"),
    Swedish(code = "sv"),
    Tajik(code = "tg"),
    Tamil(code = "ta"),
    Telugu(code = "te"),
    Thai(code = "th"),
    Turkish(code = "tr"),
    Ukrainian(code = "uk"),
    Urdu(code = "ur"),
    Uyghur(code = "ug"),
    Uzbek(code = "uz"),
    Vietnamese(code = "vi"),
    Welsh(code = "cy"),
    Xhosa(code = "xh"),
    Yiddish(code = "yi"),
    Yoruba(code = "yo"),
    Zulu(code = "zu");

    companion object {
        val nameLut = enumEntries<Language>().associateBy { it.name }
        val codeLut = enumEntries<Language>().associateBy { it.code }
    }
}

fun languageOf(tag: String): Language? = Language.nameLut[tag] ?: Language.codeLut[tag]
