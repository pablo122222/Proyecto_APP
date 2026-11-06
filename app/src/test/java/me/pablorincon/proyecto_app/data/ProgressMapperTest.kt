package me.pablorincon.proyecto_app.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProgressMapperTest {

    @Test
    fun leeLosIdsCompletados() {
        val data = mapOf<String, Any?>(
            ProgressMapper.COMPLETED_LESSONS to listOf("fundamentos_1", "fundamentos_2")
        )
        assertEquals(setOf("fundamentos_1", "fundamentos_2"), ProgressMapper.completedLessonIds(data))
    }

    @Test
    fun unDocumentoAusenteEsNingunaCompletada() {
        assertTrue(ProgressMapper.completedLessonIds(null).isEmpty())
        assertTrue(ProgressMapper.completedLessonIds(emptyMap()).isEmpty())
    }

    @Test
    fun uncampoConTipoEquivocadoEsNingunaCompletada() {
        val data = mapOf<String, Any?>(ProgressMapper.COMPLETED_LESSONS to "fundamentos_1")
        assertTrue(ProgressMapper.completedLessonIds(data).isEmpty())
    }

    @Test
    fun ignoraLosElementosQueNoSonTexto() {
        val data = mapOf<String, Any?>(
            ProgressMapper.COMPLETED_LESSONS to listOf("fundamentos_1", 7, null, "fundamentos_1")
        )
        assertEquals(setOf("fundamentos_1"), ProgressMapper.completedLessonIds(data))
    }
}
