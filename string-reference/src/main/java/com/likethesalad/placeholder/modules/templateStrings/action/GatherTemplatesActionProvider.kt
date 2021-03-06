package com.likethesalad.placeholder.modules.templateStrings.action

import com.likethesalad.placeholder.modules.common.helpers.android.AndroidVariantContext
import com.likethesalad.placeholder.modules.templateStrings.GatherTemplatesAction
import com.likethesalad.placeholder.modules.templateStrings.GatherTemplatesActionFactory
import com.likethesalad.placeholder.providers.ActionProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GatherTemplatesActionProvider @Inject
constructor(private val gatherTemplatesActionFactory: GatherTemplatesActionFactory) : ActionProvider<GatherTemplatesAction> {

    override fun provide(androidVariantContext: AndroidVariantContext): GatherTemplatesAction {
        return gatherTemplatesActionFactory.create(androidVariantContext)
    }
}