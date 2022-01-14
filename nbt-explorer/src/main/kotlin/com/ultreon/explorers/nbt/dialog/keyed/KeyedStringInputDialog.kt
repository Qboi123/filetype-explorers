package com.ultreon.explorers.nbt.dialog.keyed

import org.eclipse.jface.dialogs.IDialogConstants
import org.eclipse.jface.dialogs.TitleAreaDialog
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*

class KeyedStringInputDialog(parentShell: Shell?) : TitleAreaDialog(parentShell) {
    var key: String = ""
    var value: String = ""
    private lateinit var keyText: Text
    private lateinit var valueText: Text
    var firstName: String? = null
        private set
    var lastName: String? = null
        private set

    override fun create() {
        super.create()
        setTitle("Create New String Value")
//        setMessage("This is a TitleAreaDialog", IMessageProvider.INFORMATION)
    }

    override fun createDialogArea(parent: Composite): Control {
        val area = super.createDialogArea(parent) as Composite
        val container = Composite(area, SWT.NONE)
        container.layoutData = GridData(SWT.FILL, SWT.FILL, true, true)
        val layout = GridLayout(2, false)
        container.layout = layout
        createKeyInput(container)
        createValueInput(container)
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

    private fun createValueInput(container: Composite) {
        val lbtLastName = Label(container, SWT.NONE)
        lbtLastName.text = "String Int value"
        val valueData = GridData()
        valueData.grabExcessHorizontalSpace = true
        valueData.horizontalAlignment = GridData.FILL
        valueText = Text(container, SWT.BORDER)
        valueText.addListener(SWT.Modify) { _: Event? ->
            try {
                value = valueText.text
                getButton(IDialogConstants.OK_ID).isEnabled = true
            } catch (e: Exception) {
                getButton(IDialogConstants.OK_ID).isEnabled = false
            }
        }
        valueText.layoutData = valueData
    }

    override fun isResizable(): Boolean {
        return false
    }

    // save content of the Text fields because they get disposed
    // as soon as the Dialog closes
    private fun saveInput() {
        firstName = keyText.text
        lastName = valueText.text
    }

    override fun okPressed() {
        saveInput()
        super.okPressed()
    }
}