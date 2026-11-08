package me.pablorincon.proyecto_app.data

import me.pablorincon.proyecto_app.data.ProgressRepository.ProgressResult
import me.pablorincon.proyecto_app.data.UserRepository.ProfileResult
import me.pablorincon.proyecto_app.domain.Curriculum
import me.pablorincon.proyecto_app.domain.CurriculumProgress

// Arma el progreso del usuario: lee su nivel de Personalización (decide en qué módulo
// empieza) y sus pasos completados, y calcula todo con CurriculumProgress.
// Lo usan la Ruta y la Lección para no repetir esta lógica.
class CurriculumProgressLoader(
    private val users: UserRepository = UserRepository(),
    private val progress: ProgressRepository = ProgressRepository(),
    private val curriculum: Curriculum = Curriculum.Base
) {
    sealed interface LoadResult {
        data class Loaded(val progress: CurriculumProgress) : LoadResult
        data object Failed : LoadResult
    }

    suspend fun load(uid: String, fallbackName: String = ""): LoadResult {
        val profile = (users.ensureProfile(uid, fallbackName) as? ProfileResult.Success)?.profile
            ?: return LoadResult.Failed
        val completed = (progress.loadCompletedLessons(uid) as? ProgressResult.Success)?.completedLessonIds
            ?: return LoadResult.Failed
        return LoadResult.Loaded(
            CurriculumProgress(curriculum, completed, curriculum.startModuleIndex(profile.level))
        )
    }
}
