package com.dailystudio.devbricksx.sample.ui

import android.graphics.drawable.Drawable
import android.view.View
import com.dailystudio.devbricksx.sample.R
import com.dailystudio.devbricksx.sample.db.Notebook
import com.dailystudio.devbricksx.ui.AbsSingleLineViewHolder
import com.dailystudio.devbricksx.utils.ResourcesCompatUtils

class NotebookViewHolder(itemView: View) : AbsSingleLineViewHolder<Notebook>(itemView) {

    override fun getIcon(item: Notebook): Drawable? {
        return ResourcesCompatUtils.getDrawable(itemView.context,
                R.drawable.ic_notebook)
    }

    override fun getText(item: Notebook): CharSequence? {
        return item.name?.capitalize()
    }

}

