package com.likethesalad.placeholder.tasks.actions

import com.google.common.truth.Truth
import com.likethesalad.placeholder.data.VariantRawStrings
import com.likethesalad.placeholder.data.resources.ResourcesHandler
import com.likethesalad.placeholder.models.PathIdentity
import com.likethesalad.placeholder.models.StringResourceModel
import com.likethesalad.placeholder.models.StringsGatheredModel
import com.likethesalad.placeholder.models.ValuesStrings
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class GatherRawStringsActionTest {

    private lateinit var variantRawStrings: VariantRawStrings
    private lateinit var resourcesHandler: ResourcesHandler
    private lateinit var gatherRawStringsAction: GatherRawStringsAction

    @Before
    fun setUp() {
        variantRawStrings = mockk()
        resourcesHandler = mockk(relaxUnitFun = true)
        gatherRawStringsAction = GatherRawStringsAction(variantRawStrings, resourcesHandler)
    }

    @Test
    fun `Check saved gathered strings`() {
        val strings = listOf(StringResourceModel("the_string_name", "the string content"))
        val valuesStrings = getValuesStrings(
            "client",
            "values",
            "",
            "main",
            strings
        )

        every { variantRawStrings.valuesStrings }.returns(listOf(valuesStrings))
        val gatheredStringsCaptor = slot<StringsGatheredModel>()

        gatherRawStringsAction.gatherStrings()

        verify { resourcesHandler.saveGatheredStrings(capture(gatheredStringsCaptor)) }
        Truth.assertThat(gatheredStringsCaptor.captured).isEqualTo(
            StringsGatheredModel(
                PathIdentity("client", "values", ""),
                strings
            )
        )
    }

    @Test
    fun `Check don't save values strings with no templates nor values for templates`() {
        val strings = listOf(StringResourceModel("the_string_name", "the string content"))
        val valuesStrings = getValuesStrings(
            "client",
            "values",
            "",
            "",
            strings
        )

        every { variantRawStrings.valuesStrings }.returns(listOf(valuesStrings))

        gatherRawStringsAction.gatherStrings()

        verify(exactly = 0) { resourcesHandler.saveGatheredStrings(any()) }
    }

    @Test
    fun `Check many saved gathered strings`() {
        val valuesStringsList = listOf(
            StringResourceModel(
                "the_string_name",
                "the string content"
            )
        )
        val valuesEsStringsList = listOf(
            StringResourceModel(
                "the_es_string_name",
                "the es string content"
            )
        )
        val valuesStrings = getValuesStrings(
            "client",
            "values",
            "",
            "main",
            valuesStringsList
        )
        val valuesEsStrings = getValuesStrings(
            "client",
            "values-es",
            "-es",
            "client",
            valuesEsStringsList
        )

        every { variantRawStrings.valuesStrings }.returns(listOf(valuesStrings, valuesEsStrings))
        val gatheredStringsCaptor = mutableListOf<StringsGatheredModel>()

        gatherRawStringsAction.gatherStrings()

        verify(exactly = 2) { resourcesHandler.saveGatheredStrings(capture(gatheredStringsCaptor)) }
        Truth.assertThat(gatheredStringsCaptor.size).isEqualTo(2)
        Truth.assertThat(gatheredStringsCaptor.first()).isEqualTo(
            StringsGatheredModel(
                PathIdentity("client", "values", ""),
                valuesStringsList
            )
        )
        Truth.assertThat(gatheredStringsCaptor[1]).isEqualTo(
            StringsGatheredModel(
                PathIdentity("client", "values-es", "-es"),
                valuesEsStringsList
            )
        )
    }

    private fun getValuesStrings(
        variantName: String,
        valuesFolderName: String,
        valuesSuffix: String,
        primaryVariantName: String,
        strings: List<StringResourceModel>
    ): ValuesStrings {
        val valuesStrings = mockk<ValuesStrings>()
        every { valuesStrings.variantName }.returns(variantName)
        every { valuesStrings.valuesFolderName }.returns(valuesFolderName)
        every { valuesStrings.valuesSuffix }.returns(valuesSuffix)
        every { valuesStrings.mergedStrings }.returns(strings)
        every { valuesStrings.primaryVariantName }.returns(primaryVariantName)

        return valuesStrings
    }
}