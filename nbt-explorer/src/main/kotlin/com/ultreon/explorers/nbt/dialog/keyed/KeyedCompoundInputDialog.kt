package com.ultreon.explorers.nbt.dialog.keyed

import org.eclipse.jface.dialogs.IDialogConstants
import org.eclipse.jface.dialogs.TitleAreaDialog
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*

class KeyedCompoundInputDialog(parentShell: Shell?) : TitleAreaDialog(parentShell) {
    var key: String = ""
    private lateinit var keyText: Text
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
        lbtFirstName.text = "Compound key"
        val keyData = GridData()
        keyData.grabExcessHorizontalSpace = true
        keyData.horizontalAlignment = GridData.FILL
        keyText = Text(container, SWT.BORDER)
        keyText.addListener(SWT.Modify) { event: Event? ->
            try {
                if (keyText.text.isBlank()) {
                    throw RuntimeException()
                }
                key = keyText.text
                getButton(IDialogConstants.OK_ID).isEnabled = true
            } catch (e: Exception) {
                getButton(IDialogConstants.OK_ID).isEnabled = false
            }
        }
        keyText.layoutData = keyData
    }

    override fun isResizable(): Boolean {
        return false
    }

}