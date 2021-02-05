package com.soufianekre.cashnotes.helper

object ThemeEngine {

    /*

    fun apply(activity: AppCompatActivity) {
        val themeMode :Int = MyApp.getAppPrefs().getInt(PrefsConst.PREF_THEME,PrefsConst.LIGHT)
        val themeColor = MyApp.getAppPrefs().getInt(PrefsConst.PREF_APP_COLORS,R.color.accent_orange)
        activity.setTheme(getTheme(themeMode, themeColor))
        //setTaskDescription(activity)
        //applyNavBarColor(activity)

    }

    fun applyForAbout(activity: MaterialAboutActivity) {
        val themeMode = MyApp.getAppPrefs().getBoolean(PrefsConst.PREF_THEME,false)
        /*
        when (themeMode) {
            PrefGetter.LIGHT -> activity.setTheme(R.style.AppTheme_AboutActivity_Light)
            PrefGetter.DARK -> activity.setTheme(R.style.AppTheme_AboutActivity_Dark)
        }

         */

        //setTaskDescription(activity)
    }



    @StyleRes
    private fun getTheme(themeMode: Int, themeColor: Int): Int {
        //Timber.e("Theme %d", themeColor)
        // I wish if I could simplify this :'( too many cases for the love of god.
        when (themeMode) {
            PrefsConst.LIGHT -> when (themeColor) {
                PrefsConst.COLOR_DEFAULT -> return R.style.ThemeLight
                PrefsConst.ORANGE -> return R.style.ThemeLight_Orange
                PrefsConst.BLUE -> return R.style.ThemeLight_Blue
                PrefsConst.GREEN -> return R.style.ThemeLight_Green
                /*
                PrefsConst.RED -> return R.style.ThemeLight_Red
                PrefsConst.PINK -> return R.style.ThemeLight_Pink
                PrefsConst.PURPLE -> return R.style.ThemeLight_Purple
                PrefsConst.DEEP_PURPLE -> return R.style.ThemeLight_DeepPurple
                PrefsConst.INDIGO -> return R.style.ThemeLight_Indigo
                PrefsConst.LIGHT_BLUE -> return R.style.ThemeLight_LightBlue
                PrefsConst.CYAN -> return R.style.ThemeLight_Cyan
                PrefsConst.TEAL -> return R.style.ThemeLight_Teal
                PrefsConst.GREEN -> return R.style.ThemeLight_Green
                PrefsConst.LIGHT_GREEN -> return R.style.ThemeLight_LightGreen
                PrefsConst.LIME -> return R.style.ThemeLight_Lime
                PrefsConst.YELLOW -> return R.style.ThemeLight_Yellow
                PrefsConst.AMBER -> return R.style.ThemeLight_Amber
                PrefsConst.ORANGE -> return R.style.ThemeLight_Orange
                PrefsConst.DEEP_ORANGE -> return R.style.ThemeLight_DeepOrange

                 */
                else -> return R.style.ThemeLight
            }

            PrefsConst.DARK -> when (themeColor) {

                PrefsConst.COLOR_DEFAULT -> return R.style.ThemeDark
                PrefsConst.ORANGE -> return R.style.ThemeDark_Orange
                PrefsConst.BLUE -> return R.style.ThemeDark_Blue
                PrefsConst.GREEN -> return R.style.ThemeDark_Green
                /*
                PrefsConst.RED -> return R.style.ThemeDark_Red
                PrefsConst.PINK -> return R.style.ThemeDark_Pink
                PrefsConst.PURPLE -> return R.style.ThemeDark_Purple
                PrefsConst.DEEP_PURPLE -> return R.style.ThemeDark_DeepPurple
                PrefsConst.INDIGO -> return R.style.ThemeDark_Indigo
                PrefsConst.LIGHT_BLUE -> return R.style.ThemeDark_LightBlue
                PrefsConst.CYAN -> return R.style.ThemeDark_Cyan
                PrefsConst.TEAL -> return R.style.ThemeDark_Teal
                PrefsConst.LIGHT_GREEN -> return R.style.ThemeDark_LightGreen
                PrefsConst.LIME -> return R.style.ThemeDark_Lime
                PrefsConst.YELLOW -> return R.style.ThemeDark_Yellow
                PrefsConst.AMBER -> return R.style.ThemeDark_Amber
                PrefsConst.DEEP_ORANGE -> return R.style.ThemeDark_DeepOrange
                 */
                else -> return R.style.ThemeDark
            }

        }
        return R.style.ThemeLight_Blue
    }

    private fun getRandomMaterialColor(context: Context, typeColor: String): Int {
        var returnColor = Color.GRAY
        val arrayId = context.resources.getIdentifier("mdcolor_$typeColor", "array", context.packageName)

        if (arrayId != 0) {
            val colors = context.resources.obtainTypedArray(arrayId)
            val index = (Math.random() * colors.length()).toInt()
            returnColor = colors.getColor(index, Color.GRAY)
            colors.recycle()
        }
        return returnColor
    }

    fun getIconicDrawable(context: Context, icon: IIcon, color: Int, size: Int): Drawable {
        return IconicsDrawable(context).icon(icon).color(color).sizeDp(size)
    }

    fun getIconicDrawable(context: Context, icon: IIcon, color: Int, sizeX: Int, sizeY: Int): Drawable {
        return IconicsDrawable(context).icon(icon).color(color).sizeDpX(sizeX).sizeDpY(sizeY)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun getDrawable(context: Context, @DrawableRes drawableRes: Int): Drawable {
        return context.resources.getDrawable(drawableRes, null)
    }

    fun setToolbarBackgroundColor(context: Context, toolbar: Toolbar, color: Int) {
        toolbar.setBackgroundColor(getColor(context, color))
    }


    fun setupStatusBarColor(activity: Activity, color: Int) {
        if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (color != 0) window.statusBarColor = color

        }
    }

    fun getColor(context: Context, i: Int): Int {
        return context.resources.getColor(i)
    }

     */

}
