package me.pablorincon.proyecto_app.ui.screens.personalization

import androidx.annotation.StringRes
import me.pablorincon.proyecto_app.R
import me.pablorincon.proyecto_app.domain.LearningGoal
import me.pablorincon.proyecto_app.domain.LearningLevel

// Textos de cada opción de Personalización (todos en strings.xml).
// Se separan del modelo para que `domain` no dependa de Android.

@get:StringRes val LearningLevel.iconRes: Int
    get() = when (this) {
        LearningLevel.Beginner -> R.string.level_beginner_icon
        LearningLevel.Basic -> R.string.level_basic_icon
        LearningLevel.Intermediate -> R.string.level_intermediate_icon
    }

@get:StringRes val LearningLevel.titleRes: Int
    get() = when (this) {
        LearningLevel.Beginner -> R.string.level_beginner_title
        LearningLevel.Basic -> R.string.level_basic_title
        LearningLevel.Intermediate -> R.string.level_intermediate_title
    }

@get:StringRes val LearningLevel.subtitleRes: Int
    get() = when (this) {
        LearningLevel.Beginner -> R.string.level_beginner_subtitle
        LearningLevel.Basic -> R.string.level_basic_subtitle
        LearningLevel.Intermediate -> R.string.level_intermediate_subtitle
    }

// Valor que se muestra en "Ruta lista" (Principiante / Básico / Intermedio).
@get:StringRes val LearningLevel.valueRes: Int
    get() = when (this) {
        LearningLevel.Beginner -> R.string.level_beginner_value
        LearningLevel.Basic -> R.string.level_basic_value
        LearningLevel.Intermediate -> R.string.level_intermediate_value
    }

@get:StringRes val LearningGoal.iconRes: Int
    get() = when (this) {
        LearningGoal.UnderstandProgramming -> R.string.goal_understand_icon
        LearningGoal.PassCourse -> R.string.goal_pass_icon
        LearningGoal.SolveProblems -> R.string.goal_solve_icon
        LearningGoal.BuildProjects -> R.string.goal_build_icon
    }

// El objetivo no tiene un valor corto: "Ruta lista" muestra el mismo título de la opción.
@get:StringRes val LearningGoal.titleRes: Int
    get() = when (this) {
        LearningGoal.UnderstandProgramming -> R.string.goal_understand_title
        LearningGoal.PassCourse -> R.string.goal_pass_title
        LearningGoal.SolveProblems -> R.string.goal_solve_title
        LearningGoal.BuildProjects -> R.string.goal_build_title
    }

@get:StringRes val LearningGoal.subtitleRes: Int
    get() = when (this) {
        LearningGoal.UnderstandProgramming -> R.string.goal_understand_subtitle
        LearningGoal.PassCourse -> R.string.goal_pass_subtitle
        LearningGoal.SolveProblems -> R.string.goal_solve_subtitle
        LearningGoal.BuildProjects -> R.string.goal_build_subtitle
    }
