package com.ultreon.explorers.nbt.dialog.indexed

import net.querz.nbt.tag.*
import org.eclipse.jface.dialogs.IDialogConstants
import org.eclipse.jface.dialogs.TitleAreaDialog
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*
import org.eclipse.swt.widgets.List

class IndexedListInputDialog(parentShell: Shell?) : TitleAreaDialog(parentShell) {
    private val map: Map<String, Class<out Tag<*>>> = mapOf(
        Pair("Compound", CompoundTag::class.java),
        Pair("List", ListTag::class.java),
        Pair("Byte Array", ByteArrayTag::class.java),
        Pair("Integer Array", IntArrayTag::class.java),
        Pair("Long Array", LongArrayTag::class.java),
        Pair("Byte", ByteTag::class.java),
        Pair("Short Int", ShortTag::class.java),
        Pair("Integer", IntTag::class.java),
        Pair("Long", LongTag::class.java),
        Pair("Float", FloatTag::class.java),
        Pair("Double", DoubleTag::class.java),
        Pair("String", StringTag::class.java),
    )
    var index: Int = 0
    lateinit var type: Class<out Tag<*>>
    private lateinit var indexText: Text
    private lateinit var typeList: List
    var firstName: String? = null
        private set
    var lastName: String? = null
        private set

    override fun create() {
        super.create()
        setTitle("Create New List Value")
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
        lbtLastName.text = "String Int value"
        val valueData = GridData()
        valueData.grabExcessHorizontalSpace = true
        valueData.horizontalAlignment = GridData.FILL
        typeList = List(container, SWT.BORDER)
        for (type: String in map.keys) {
            typeList.add(type)
        }
        typeList.addListener(SWT.Modify) {
            try {
                type = map[typeList.selection[0]]!!
                getButton(IDialogConstants.OK_ID).isEnabled = true
            } catch (e: Exception) {
                getButton(IDialogConstants.OK_ID).isEnabled = false
            }
        }
        typeList.layoutData = valueData
    }

    override fun isResizable(): Boolean {
        return false
    }

}