package com.ultreon.explorers.nbt.dialog.indexed

import org.eclipse.jface.dialogs.IDialogConstants
import org.eclipse.jface.dialogs.TitleAreaDialog
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*

class IndexedCompoundInputDialog(parentShell: Shell?) : TitleAreaDialog(parentShell) {
    var index: Int = 0
    private lateinit var indexText: Text
    var firstName: String? = null
        private set
    var lastName: String? = null
        private set

    override fun create() {
        super.create()
        setTitle("Create New Compound Value")
//        setMessage("This is a TitleAreaDialog", IMessageProvider.INFORMATION)
    }

    override fun createDialogArea(parent: Composite): Control {
        val area = super.createDialogArea(parent) as Composite
        val container = Composite(area, SWT.NONE)
        container.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)
        val layout = GridLayout(2, false)
        container.layout = layout
        createKeyInput(container)
        return area
    }

    private fun createKeyInput(container: Composite) {
        val lbtFirstName = Label(container, SWT.NONE)
        lbtFirstName.text = "List index"
        val indexData = GridData()
        indexData.grabExcessHorizontalSpace = true
        indexData.horizontalAlignment = GridData.FILL
        indexText = Text(container, SWT.BORDER)
        indexText.addListener(SWT.Modify) { event: Event? ->
            try {
                index = indexText.text.toInt()
                getButton(IDialogConstants.OK_ID).isEnabled = true
            } catch (e: Exception) {
                getButton(IDialogConstants.OK_ID).isEnabled = false
            }
        }
        indexText.layoutData = indexData
    }

    override fun isResizable(): Boolean {
        return false
    }

}