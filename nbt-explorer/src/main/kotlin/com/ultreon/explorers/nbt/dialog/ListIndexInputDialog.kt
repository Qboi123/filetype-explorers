package com.ultreon.explorers.nbt.dialog

import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.Point
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*

@Deprecated("Replaced with Indexed / Keyed dialogs.")
open class ListIndexInputDialog : IntInputDialog {
    /**
     * @param parent parent
     */
    constructor(parent: Shell?) : super(parent)

    /**
     * @param parent parent
     * @param style style
     */
    constructor(parent: Shell?, style: Int) : super(parent, style)

    /**
     * Makes the dialog visible.
     *
     * @return value
     */
    override fun open(): Int? {
        val parent = parent
        val shell = Shell(parent, SWT.TITLE or SWT.BORDER or SWT.APPLICATION_MODAL)
        shell.text = "NumberInputDialog"
        shell.layout = GridLayout(2, true)
        val label = Label(shell, SWT.NULL)
        label.text = "List index:"
        val text = Text(shell, SWT.SINGLE or SWT.BORDER)
        text.size = Point(100, text.size.y)
        val buttonOK = Button(shell, SWT.PUSH)
        buttonOK.text = "Ok"
        buttonOK.layoutData = GridData(GridData.HORIZONTAL_ALIGN_END)
        val buttonCancel = Button(shell, SWT.PUSH)
        buttonCancel.text = "Cancel"
        text.addListener(SWT.Modify) { event: Event? ->
            try {
                value = text.text.toInt()
                buttonOK.isEnabled = true
            } catch (e: Exception) {
                buttonOK.isEnabled = false
            }
        }
        buttonOK.addListener(SWT.Selection) { event: Event? -> shell.dispose() }
        buttonCancel.addListener(SWT.Selection) { event: Event? ->
            value = null
            shell.dispose()
        }
        shell.addListener(SWT.Traverse) { event: Event -> if (event.detail == SWT.TRAVERSE_ESCAPE) event.doit = false }
        text.text = ""
        shell.pack()
        shell.open()
        val display = parent.display
        while (!shell.isDisposed) {
            if (!display.readAndDispatch()) display.sleep()
        }
        return value
    }
}