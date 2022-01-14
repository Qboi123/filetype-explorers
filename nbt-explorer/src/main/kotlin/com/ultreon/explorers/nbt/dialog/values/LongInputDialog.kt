package com.ultreon.explorers.nbt.dialog.values

import org.eclipse.jface.dialogs.IDialogConstants
import org.eclipse.jface.dialogs.TitleAreaDialog
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*

class LongInputDialog(parentShell: Shell?) : TitleAreaDialog(parentShell) {
    var index: Int = 0
    var value: Long = 0
    private lateinit var indexText: Text
    private lateinit var valueText: Text
    var firstName: String? = null
        private set
    var lastName: String? = null
        private set

    override fun create() {
        super.create()
        setTitle("Create New Long Value")
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

    private fun createValueInput(container: Composite) {
        val lbtLastName = Label(container, SWT.NONE)
        lbtLastName.text = "Long value"
        val valueData = GridData()
        valueData.grabExcessHorizontalSpace = true
        valueData.horizontalAlignment = GridData.FILL
        valueText = Text(container, SWT.BORDER)
        valueText.addListener(SWT.Modify) { _: Event? ->
            try {
                value = valueText.text.toLong()
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
        firstName = indexText.text
        lastName = valueText.text
    }

    override fun okPressed() {
        saveInput()
        super.okPressed()
    }
}