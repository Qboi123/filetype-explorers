package com.ultreon.explorers.nbt

import com.ultreon.explorers.ExplorerWindow
import com.ultreon.explorers.nbt.dialog.CompoundKeyInputDialog
import com.ultreon.explorers.nbt.dialog.ListDialog
import com.ultreon.explorers.nbt.dialog.indexed.*
import com.ultreon.explorers.nbt.dialog.keyed.*
import net.querz.nbt.io.NBTUtil
import net.querz.nbt.tag.*
import org.eclipse.jface.dialogs.Dialog
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.widgets.Menu
import org.eclipse.swt.widgets.MenuItem
import org.eclipse.swt.widgets.TreeColumn
import org.eclipse.swt.widgets.TreeItem
import java.io.File

/**
 * Main kotlin file.
 *
 * @author Qboi123
 */
class NbtExplorer(file: File?) : ExplorerWindow(file) {
    companion object {
        /**
         * @author Qboi123
         * @param args Run arguments.
         */
        @JvmStatic
        fun main(vararg args: String) {
            var file: File? = null
            if (args.isNotEmpty()) {
                file = File(args[0])
            }

            val nbt = CompoundTag()
            nbt["integer"] = 3
            nbt["long"] = 3L
            nbt["string"] = "Hello"
            val compound = CompoundTag()
            compound["integer"] = 512
            compound["long"] = 6473243649523423L
            compound["string"] = "Test"
            val subCompound = CompoundTag()
            subCompound["integer"] = 512
            subCompound["long"] = 6473243649523423L
            subCompound["string"] = "Test"
            compound["SubCompound"] = subCompound
            nbt["Compound"] = compound

            val explorer = NbtExplorer(file)
            explorer.postInit()
        }
    }

    private var rootItem: TreeItem? = null
    var icons: Array<Image>
        private set
    private var typeCol: TreeColumn
    private var valueCol: TreeColumn

    private var compoundMenuItem: MenuItem
    private var listMenuItem: MenuItem
    private val byteMenuItem: MenuItem
    private var shortMenuItem: MenuItem
    private val intMenuItem: MenuItem
    private val longMenuItem: MenuItem
    private val floatMenuItem: MenuItem
    private val doubleMenuItem: MenuItem
    private val stringMenuItem: MenuItem

    init {
        shell.text = "NBT Explorer"
        icons = loadMultiResIcon("Icon.ico")
        shell.images = icons
        typeCol = TreeColumn(tree, SWT.LEFT)
        typeCol.text = "Type"
        typeCol.width = 100
        typeCol.moveable = true
        typeCol.resizable = false
        valueCol = TreeColumn(tree, SWT.RIGHT)
        valueCol.text = "Value"
        valueCol.width = 200
        valueCol.moveable = true
        valueCol.resizable = true

        val createMenuItem = MenuItem(rootMenu, SWT.CASCADE)
        createMenuItem.text = "&Create"
        val createMenu = Menu(shell, SWT.DROP_DOWN)
        createMenuItem.menu = createMenu

        compoundMenuItem = MenuItem(createMenu, SWT.PUSH)
        compoundMenuItem.text = "New &Compound"
        compoundMenuItem.setData("OnWidgetSelected", Runnable(::createCompound))
        compoundMenuItem.addSelectionListener(MenuBarEventHandlers())

        listMenuItem = MenuItem(createMenu, SWT.PUSH)
        listMenuItem.text = "New &List"
        listMenuItem.setData("OnWidgetSelected", Runnable(::createList))
        listMenuItem.addSelectionListener(MenuBarEventHandlers())

        byteMenuItem = MenuItem(createMenu, SWT.PUSH)
        byteMenuItem.text = "New &Byte"
        byteMenuItem.setData("OnWidgetSelected", Runnable(::createByte))
        byteMenuItem.addSelectionListener(MenuBarEventHandlers())

        shortMenuItem = MenuItem(createMenu, SWT.PUSH)
        shortMenuItem.text = "New &Short Int"
        shortMenuItem.setData("OnWidgetSelected", Runnable(::createShort))
        shortMenuItem.addSelectionListener(MenuBarEventHandlers())

        intMenuItem = MenuItem(createMenu, SWT.PUSH)
        intMenuItem.text = "New &Int"
        intMenuItem.setData("OnWidgetSelected", Runnable(::createInt))
        intMenuItem.addSelectionListener(MenuBarEventHandlers())

        longMenuItem = MenuItem(createMenu, SWT.PUSH)
        longMenuItem.text = "New L&ong"
        longMenuItem.setData("OnWidgetSelected", Runnable(::createLong))
        longMenuItem.addSelectionListener(MenuBarEventHandlers())

        floatMenuItem = MenuItem(createMenu, SWT.PUSH)
        floatMenuItem.text = "New &Float"
        floatMenuItem.setData("OnWidgetSelected", Runnable(::createFloat))
        floatMenuItem.addSelectionListener(MenuBarEventHandlers())

        doubleMenuItem = MenuItem(createMenu, SWT.PUSH)
        doubleMenuItem.text = "New &Double"
        doubleMenuItem.setData("OnWidgetSelected", Runnable(::createDouble))
        doubleMenuItem.addSelectionListener(MenuBarEventHandlers())

        stringMenuItem = MenuItem(createMenu, SWT.PUSH)
        stringMenuItem.text = "New &String"
        stringMenuItem.setData("OnWidgetSelected", Runnable(::createString))
        stringMenuItem.addSelectionListener(MenuBarEventHandlers())
    }

    private fun createCompound() {
        val selection = tree.selection
        if (selection.isEmpty()) {
            return
        }
        val selected = selection[0]
        val data = selected.getData("NBTData")
        if (data is CompoundTag) {
            val input = KeyedCompoundInputDialog(shell)
            if (input.open() == Dialog.OK) {
                val key = input.key

                val compound = CompoundTag()
                data[key] = compound

                var prevIndex: Int
                var index = 0
                for ((i, item) in selected.items.withIndex()) {
                    prevIndex = i
                    val dat = item.getData("NBTData")
                    if (dat is CompoundTag) {
                        if (item.getText(0) > key) {
                            index = prevIndex
                            break
                        }
                    } else {
                        index = prevIndex
                        break
                    }
                }
                val treeItem = TreeItem(selected, SWT.NONE, index)
                treeItem.setText(arrayOf(key, "Compound", ""))
                treeItem.setData("NBTData", data[key])
                treeItem.image = compoundImage
            }
        } else if (data is ListTag<out Tag<*>>) {
            val input = IndexedCompoundInputDialog(shell)

            if (input.open() == Dialog.OK) {
                val index = input.index

                if (data.typeClass == CompoundTag::class.java) {
                    @Suppress("UNCHECKED_CAST") val tags = data as ListTag<CompoundTag>

                    val compound = CompoundTag()
                    tags.add(index, compound)

                    val treeItem = TreeItem(selected, SWT.NONE, index)
                    treeItem.setText(arrayOf(index.toString(), "Compound", ""))
                    treeItem.setData("NBTData", data[index])
                    treeItem.image = listImage
                }
            }
        }
    }

    private fun createList() {
        val selection = tree.selection
        if (selection.isEmpty()) {
            return
        }
        val selected = selection[0]
        val data = selected.getData("NBTData")
        if (data is CompoundTag) {
            val input = KeyedListInputDialog(shell)
            if (input.open() == Dialog.OK) {
                val key = input.key
                val type = input.type

                val list: ListTag<out Tag<*>> = ListTag(type)
                data[key] = list

                var prevIndex = 0
                var index = 0
                for ((i, item) in selected.items.withIndex()) {
                    val dat = item.getData("NBTData")
                    if (dat !is CompoundTag) {
                        if (dat is ListTag<*>) {
                            if (item.getText(0) > key) {
                                index = prevIndex
                                break
                            }
                        } else {
                            index = prevIndex
                            break
                        }
                    }
                    prevIndex = i
                }
                val treeItem = TreeItem(selected, SWT.NONE, index)
                treeItem.setText(arrayOf(key, "List", ""))
                treeItem.setData("NBTData", data[key])
                treeItem.image = listImage
            }
        } else if (data is ListTag<out Tag<*>>) {
            val input = IndexedListInputDialog(shell)
            if (input.open() == Dialog.OK) {
                val index = input.index
                val type = input.type

                if (data.typeClass == ListTag::class.java) {
                    @Suppress("UNCHECKED_CAST") val tags = data as ListTag<ListTag<*>>

                    val list: ListTag<out Tag<*>> = ListTag(type)
                    tags.add(index, list)

                    val treeItem = TreeItem(selected, SWT.NONE, index)
                    treeItem.setText(arrayOf(index.toString(), "List", ""))
                    treeItem.setData("NBTData", data[index])
                    treeItem.image = compoundImage
                }
            }
        }
    }

    private fun createByte() {
        val selection = tree.selection
        if (selection.isEmpty()) {
            return
        }
        val selected = selection[0]
        val data = selected.getData("NBTData")
        if (data is CompoundTag) {
            val input = KeyedByteInputDialog(shell)
            if (input.open() == Dialog.OK) {
                val key = input.key
                val value = input.value

                data[key] = value

                var prevIndex = 0
                var index: Int? = null
                for ((i, item) in selected.items.withIndex()) {
                    val dat = item.getData("NBTData")
                    if (dat !is CompoundTag && dat !is ListTag<*>) {
                        if (item.getText(0) > key) {
                            index = prevIndex
                            break
                        }
                    }
                    prevIndex = i
                }
                val treeItem: TreeItem =
                    if (index == null) TreeItem(selected, SWT.NONE) else TreeItem(selected, SWT.NONE, index)
                treeItem.setText(arrayOf(key, "Byte", data[key].valueToString()))
                treeItem.setData("NBTData", data[key])
                treeItem.image = byteImage
            }
        } else if (data is ListTag<out Tag<*>>) {
            val input = IndexedByteInputDialog(shell)

            if (input.open() == Dialog.OK) {
                val index = input.index

                if (data.typeClass == CompoundTag::class.java) {
                    @Suppress("UNCHECKED_CAST") val tags = data as ListTag<CompoundTag>

                    val compound = CompoundTag()
                    tags.add(index, compound)

                    val treeItem = TreeItem(selected, SWT.NONE)
                    treeItem.setText(arrayOf(index.toString(), "Byte", data[index].valueToString()))
                    treeItem.setData("NBTData", data[index])
                    treeItem.image = byteImage
                }
            }
        }
    }

    private fun createShort() {
        val selection = tree.selection
        if (selection.isEmpty()) {
            return
        }
        val selected = selection[0]
        val data = selected.getData("NBTData")
        if (data is CompoundTag) {
            val input = KeyedShortInputDialog(shell)
            if (input.open() == Dialog.OK) {
                val key = input.key
                val value = input.value

                data[key] = value

                var prevIndex = 0
                var index: Int? = null
                for ((i, item) in selected.items.withIndex()) {
                    val dat = item.getData("NBTData")
                    if (dat !is CompoundTag && dat !is ListTag<*>) {
                        if (item.getText(0) > key) {
                            index = prevIndex
                            break
                        }
                    }
                    prevIndex = i
                }
                val treeItem: TreeItem =
                    if (index == null) TreeItem(selected, SWT.NONE) else TreeItem(selected, SWT.NONE, index)
                treeItem.setText(arrayOf(key, "Short Int", data[key].valueToString()))
                treeItem.setData("NBTData", data[key])
                treeItem.image = shortImage
            }
        } else if (data is ListTag<out Tag<*>>) {
            val input = IndexedShortInputDialog(shell)

            if (input.open() == Dialog.OK) {
                val index = input.index

                if (data.typeClass == CompoundTag::class.java) {
                    @Suppress("UNCHECKED_CAST") val tags = data as ListTag<CompoundTag>

                    val compound = CompoundTag()
                    tags.add(index, compound)

                    val treeItem = TreeItem(selected, SWT.NONE)
                    treeItem.setText(arrayOf(index.toString(), "Short Int", data[index].valueToString()))
                    treeItem.setData("NBTData", data[index])
                    treeItem.image = shortImage
                }
            }
        }
    }

    private fun createInt() {
        val selection = tree.selection
        if (selection.isEmpty()) {
            return
        }
        val selected = selection[0]
        val data = selected.getData("NBTData")
        if (data is CompoundTag) {
            val input = KeyedIntInputDialog(shell)
            if (input.open() == Dialog.OK) {
                val key = input.key
                val value = input.value

                data[key] = value

                var prevIndex = 0
                var index: Int? = null
                for ((i, item) in selected.items.withIndex()) {
                    val dat = item.getData("NBTData")
                    if (dat !is CompoundTag && dat !is ListTag<*>) {
                        if (item.getText(0) > key) {
                            index = prevIndex
                            break
                        }
                    }
                    prevIndex = i
                }
                val treeItem: TreeItem =
                    if (index == null) TreeItem(selected, SWT.NONE) else TreeItem(selected, SWT.NONE, index)
                treeItem.setText(arrayOf(key, "Integer", data[key].valueToString()))
                treeItem.setData("NBTData", data[key])
                treeItem.image = intImage
            }
        } else if (data is ListTag<out Tag<*>>) {
            val input = IndexedIntInputDialog(shell)

            if (input.open() == Dialog.OK) {
                val index = input.index

                if (data.typeClass == CompoundTag::class.java) {
                    @Suppress("UNCHECKED_CAST") val tags = data as ListTag<CompoundTag>

                    val compound = CompoundTag()
                    tags.add(index, compound)

                    val treeItem = TreeItem(selected, SWT.NONE)
                    treeItem.setText(arrayOf(index.toString(), "Integer", data[index].valueToString()))
                    treeItem.setData("NBTData", data[index])
                    treeItem.image = intImage
                }
            }
        }
    }

    @Suppress("USELESS_ELVIS", "UNUSED_VARIABLE", "unused")
    private fun createIntArray() {
        val selection = tree.selection
        if (selection.isEmpty()) {
            return
        }
        val selected = selection[0]
        val data = selected.getData("NBTData")
        if (data is CompoundTag) {
            val key = CompoundKeyInputDialog(shell).open() ?: return
            val listDialog = ListDialog(shell)
            val value = ListDialog(shell).open() ?: return

//            data[key] = value
//
//            var prevIndex: Int = 0
//            var index: Int? = null
//            for ((i, item) in selected.items.withIndex()) {
//                val dat = item.getData("NBTData")
//                if (dat !is CompoundTag && dat !is ListTag<*>) {
//                    if (item.getText(0) > key) {
//                        index = prevIndex
//                        break
//                    }
//                }
//                prevIndex = i
//            }
//            val treeItem: TreeItem = if (index == null) TreeItem(selected, SWT.NONE) else TreeItem(selected, SWT.NONE, index)
//            treeItem.setText(arrayOf(key, "Integer", ""))
//            treeItem.image = compoundImage
        }
    }

    private fun createLong() {
        val selection = tree.selection
        if (selection.isEmpty()) {
            return
        }
        val selected = selection[0]
        val data = selected.getData("NBTData")
        if (data is CompoundTag) {
            val input = KeyedLongInputDialog(shell)
            if (input.open() == Dialog.OK) {
                val key = input.key
                val value = input.value

                data[key] = value

                var prevIndex = 0
                var index: Int? = null
                for ((i, item) in selected.items.withIndex()) {
                    val dat = item.getData("NBTData")
                    if (dat !is CompoundTag && dat !is ListTag<*>) {
                        if (item.getText(0) > key) {
                            index = prevIndex
                            break
                        }
                    }
                    prevIndex = i
                }
                val treeItem: TreeItem =
                    if (index == null) TreeItem(selected, SWT.NONE) else TreeItem(selected, SWT.NONE, index)
                treeItem.setText(arrayOf(key, "Long", data[key].valueToString()))
                treeItem.setData("NBTData", data[key])
                treeItem.image = longImage
            }
        } else if (data is ListTag<out Tag<*>>) {
            val input = IndexedLongInputDialog(shell)

            if (input.open() == Dialog.OK) {
                val index = input.index

                if (data.typeClass == CompoundTag::class.java) {
                    @Suppress("UNCHECKED_CAST") val tags = data as ListTag<CompoundTag>

                    val compound = CompoundTag()
                    tags.add(index, compound)

                    val treeItem = TreeItem(selected, SWT.NONE)
                    treeItem.setText(arrayOf(index.toString(), "Long", data[index].valueToString()))
                    treeItem.setData("NBTData", data[index])
                    treeItem.image = longImage
                }
            }
        }
    }

    private fun createFloat() {
        val selection = tree.selection
        if (selection.isEmpty()) {
            return
        }
        val selected = selection[0]
        val data = selected.getData("NBTData")

        if (data is CompoundTag) {
            val input = KeyedFloatInputDialog(shell)
            if (input.open() == Dialog.OK) {
                val key = input.key
                val value = input.value

                data[key] = value

                var prevIndex = 0
                var index: Int? = null
                for ((i, item) in selected.items.withIndex()) {
                    val dat = item.getData("NBTData")
                    if (dat !is CompoundTag && dat !is ListTag<*>) {
                        if (item.getText(0) > key) {
                            index = prevIndex
                            break
                        }
                    }
                    prevIndex = i
                }
                val treeItem: TreeItem =
                    if (index == null) TreeItem(selected, SWT.NONE) else TreeItem(selected, SWT.NONE, index)
                treeItem.setText(arrayOf(key, "Float", data[key].valueToString()))
                treeItem.setData("NBTData", data[key])
                treeItem.image = floatImage
            }
        } else if (data is ListTag<out Tag<*>>) {
            val input = IndexedFloatInputDialog(shell)

            if (input.open() == Dialog.OK) {
                val index = input.index

                if (data.typeClass == CompoundTag::class.java) {
                    @Suppress("UNCHECKED_CAST") val tags = data as ListTag<CompoundTag>

                    val compound = CompoundTag()
                    tags.add(index, compound)

                    val treeItem = TreeItem(selected, SWT.NONE)
                    treeItem.setText(arrayOf(index.toString(), "Float", data[index].valueToString()))
                    treeItem.setData("NBTData", data[index])
                    treeItem.image = floatImage
                }
            }
        }
    }

    private fun createDouble() {
        val selection = tree.selection
        if (selection.isEmpty()) {
            return
        }
        val selected = selection[0]
        val data = selected.getData("NBTData")
        if (data is CompoundTag) {
            val input = KeyedDoubleInputDialog(shell)
            if (input.open() == Dialog.OK) {
                val key = input.key
                val value = input.value

                data[key] = value

                var prevIndex = 0
                var index: Int? = null
                for ((i, item) in selected.items.withIndex()) {
                    val dat = item.getData("NBTData")
                    if (dat !is CompoundTag && dat !is ListTag<*>) {
                        if (item.getText(0) > key) {
                            index = prevIndex
                            break
                        }
                    }
                    prevIndex = i
                }
                val treeItem: TreeItem =
                    if (index == null) TreeItem(selected, SWT.NONE) else TreeItem(selected, SWT.NONE, index)
                treeItem.setText(arrayOf(key, "Double", data[key].valueToString()))
                treeItem.setData("NBTData", data[key])
                treeItem.image = doubleImage
            }
        } else if (data is ListTag<out Tag<*>>) {
            val input = IndexedDoubleInputDialog(shell)

            if (input.open() == Dialog.OK) {
                val index = input.index

                if (data.typeClass == CompoundTag::class.java) {
                    @Suppress("UNCHECKED_CAST") val tags = data as ListTag<CompoundTag>

                    val compound = CompoundTag()
                    tags.add(index, compound)

                    val treeItem = TreeItem(selected, SWT.NONE)
                    treeItem.setText(arrayOf(index.toString(), "Double", data[index].valueToString()))
                    treeItem.setData("NBTData", data[index])
                    treeItem.image = doubleImage
                }
            }
        }
    }

    private fun createString() {
        val selection = tree.selection
        if (selection.isEmpty()) {
            return
        }
        val selected = selection[0]
        val data = selected.getData("NBTData")
        if (data is CompoundTag) {
            val input = KeyedStringInputDialog(shell)
            if (input.open() == Dialog.OK) {
                val key = input.key
                val value = input.value

                data[key] = value

                var prevIndex = 0
                var index: Int? = null
                for ((i, item) in selected.items.withIndex()) {
                    val dat = item.getData("NBTData")
                    if (dat !is CompoundTag && dat !is ListTag<*>) {
                        if (item.getText(0) > key) {
                            index = prevIndex
                            break
                        }
                    }
                    prevIndex = i
                }
                val treeItem: TreeItem =
                    if (index == null) TreeItem(selected, SWT.NONE) else TreeItem(selected, SWT.NONE, index)
                treeItem.setText(arrayOf(key, "String / Text", data[key].valueToString()))
                treeItem.setData("NBTData", data[key])
                treeItem.image = textImage
            }
        } else if (data is ListTag<out Tag<*>>) {
            val input = IndexedStringInputDialog(shell)

            if (input.open() == Dialog.OK) {
                val index = input.index

                if (data.typeClass == CompoundTag::class.java) {
                    @Suppress("UNCHECKED_CAST") val tags = data as ListTag<CompoundTag>

                    val compound = CompoundTag()
                    tags.add(index, compound)

                    val treeItem = TreeItem(selected, SWT.NONE)
                    treeItem.setText(arrayOf(index.toString(), "String / Text", data[index].valueToString()))
                    treeItem.setData("NBTData", data[index])
                    treeItem.image = textImage
                }
            }
        }
    }

    override fun addTreeItems() {
        saveLocation?.let {
            onOpen(it)
        }
    }

    override fun onNew() {
        val item = TreeItem(tree, SWT.NONE)
        item.setImage(arrayOf(packageImage))
        item.setText(arrayOf("<<New File>>"))
        item.setData("NBTData", CompoundTag())
        item.setData("ShouldGenerateSubItems", true)
        rootItem = item
    }

    override fun onReset() {

    }

    override fun onOpen(file: File) {
        val nbt = NBTUtil.read(file).tag

        val item = TreeItem(tree, SWT.NONE)
        item.setImage(arrayOf(packageImage))
        item.setText(arrayOf(file.path, "File", ""))
        item.setData("NBTData", nbt)
        item.setData("ShouldGenerateSubItems", true)
        rootItem = item

        if (nbt is CompoundTag) {
            initCompoundTag(nbt, item)
        }
    }

    override fun onSave() {
        saveLocation?.let {
            rootItem?.let { rootItem ->
                val data = rootItem.getData("NBTData") as Tag<*>
                NBTUtil.write(data, it)
            }
        }
    }

    override fun generateSubItems(item: TreeItem) {
        val data: Any? = item.getData("NBTData")
        if (data != null) {
            when (data) {
                is CompoundTag -> {
                    initCompoundTag(data, item)
                }
                is ListTag<*> -> {
                    initListTag(data, item)
                }
                is ByteArrayTag -> {
                    initByteArrayTag(data, item)
                }
                is IntArrayTag -> {
                    initIntArrayTag(data, item)
                }
                is LongArrayTag -> {
                    initLongArrayTag(data, item)
                }
            }
        }
    }

    private fun initCompoundTag(tag: CompoundTag, parent: TreeItem) {
        val sortedCompound = tag.entrySet().sortedWith(kotlin.Comparator { a, b ->
            when (a.value) {
                is CompoundTag -> {
                    when (b.value) {
                        is CompoundTag -> {
                            return@Comparator a.key.compareTo(b.key)
                        }
                        else -> {
                            return@Comparator -1
                        }
                    }
                }
                is ListTag<*> -> {
                    when (b.value) {
                        is CompoundTag -> {
                            return@Comparator 1
                        }
                        is ListTag<*> -> {
                            return@Comparator a.key.compareTo(b.key)
                        }
                        else -> {
                            return@Comparator -1
                        }
                    }
                }
                is ByteArrayTag,
                is IntArrayTag,
                is LongArrayTag -> {
                    when (b.value) {
                        is CompoundTag,
                        is ListTag<*> -> {
                            return@Comparator 1
                        }
                        is ByteArrayTag,
                        is IntArrayTag,
                        is LongArrayTag -> {
                            return@Comparator a.key.compareTo(b.key)
                        }
                        else -> {
                            return@Comparator -1
                        }
                    }
                }
                else -> {
                    when (b.value) {
                        is CompoundTag,
                        is ListTag<*>,
                        is ByteArrayTag,
                        is IntArrayTag,
                        is LongArrayTag -> {
                            return@Comparator 1
                        }
                        else -> {
                            return@Comparator a.key.compareTo(b.key)
                        }
                    }
                }
            }
        })
        for ((key, value) in sortedCompound) {
            loadNbtIntoTree(key, value, parent)
        }
    }

    private fun initListTag(tag: ListTag<*>, parent: TreeItem) {
        for ((index, value) in tag.withIndex()) {
            loadNbtIntoTree(index.toString(), value, parent)
        }
    }

    private fun initByteArrayTag(tag: ByteArrayTag, parent: TreeItem) {
        for ((index, value) in tag.value.withIndex()) {
            val subItem = TreeItem(parent, SWT.NONE)
            subItem.setData("ShouldGenerateSubItems", false)
            subItem.setImage(arrayOf(byteImage))
            subItem.setText(arrayOf(index.toString(), "Byte", value.toString()))
            subItem.setData("NBTArrayEntry", value)
        }
    }

    private fun initIntArrayTag(tag: IntArrayTag, parent: TreeItem) {
        for ((index, value) in tag.value.withIndex()) {
            val subItem = TreeItem(parent, SWT.NONE)
            subItem.setData("ShouldGenerateSubItems", false)
            subItem.setImage(arrayOf(intImage))
            subItem.setText(arrayOf(index.toString(), "Integer", value.toString()))
            subItem.setData("NBTArrayEntry", value)
        }
    }

    private fun initLongArrayTag(tag: LongArrayTag, parent: TreeItem) {
        for ((index, value) in tag.value.withIndex()) {
            val subItem = TreeItem(parent, SWT.NONE)
            subItem.setData("ShouldGenerateSubItems", false)
            subItem.setImage(arrayOf(longImage))
            subItem.setText(arrayOf(index.toString(), "Long", value.toString()))
            subItem.setData("NBTArrayEntry", value)
        }
    }

    private fun loadNbtIntoTree(key: String, value: Tag<*>, parent: TreeItem) {
        val subItem = TreeItem(parent, SWT.NONE)
        subItem.setData("NBTData", value)
        subItem.setData("ShouldGenerateSubItems", false)
        when (value) {
            is CompoundTag -> {
                loadCompound(key, subItem)
            }
            is ListTag<*> -> {
                loadList(key, subItem)
            }
            is ByteTag -> {
                loadByte(key, subItem, value)
            }
            is ShortTag -> {
                loadShort(key, subItem, value)
            }
            is IntTag -> {
                loadInt(key, subItem, value)
            }
            is LongTag -> {
                loadLong(key, subItem, value)
            }
            is FloatTag -> {
                loadFloat(key, subItem, value)
            }
            is DoubleTag -> {
                loadDouble(key, subItem, value)
            }
            is StringTag -> {
                loadString(key, subItem, value)
            }
            is ByteArrayTag -> {
                loadByteArray(key, subItem, value)
            }
            is IntArrayTag -> {
                loadIntArray(key, subItem, value)
            }
            is LongArrayTag -> {
                loadLongArray(key, subItem, value)
            }
            else -> {
                subItem.setImage(arrayOf(unknownImage))
                subItem.setText(arrayOf(key, value.valueToString()))
            }
        }
    }

    private fun loadLongArray(name: String, item: TreeItem, value: LongArrayTag) {
        item.setImage(arrayOf(longArrayImage))
        item.setText(arrayOf(name, "Long Array", value.valueToString()))
        item.setData("ShouldGenerateSubItems", true)
    }

    private fun loadIntArray(name: String, item: TreeItem, value: IntArrayTag) {
        item.setImage(arrayOf(intArrayImage))
        item.setText(arrayOf(name, "Integer Array", value.valueToString()))
        item.setData("ShouldGenerateSubItems", true)
    }

    private fun loadByteArray(name: String, item: TreeItem, value: ByteArrayTag) {
        item.setImage(arrayOf(byteArrayImage))
        item.setText(arrayOf(name, "Byte Array", value.valueToString()))
        item.setData("ShouldGenerateSubItems", true)
    }

    private fun loadByte(name: String, item: TreeItem, value: ByteTag) {
        item.setImage(arrayOf(byteImage))
        item.setText(arrayOf(name, "Byte", value.valueToString()))
    }

    private fun loadShort(name: String, item: TreeItem, value: ShortTag) {
        item.setImage(arrayOf(shortImage))
        item.setText(arrayOf(name, "Short Int", value.valueToString()))
    }

    private fun loadInt(name: String, item: TreeItem, value: IntTag) {
        item.setImage(arrayOf(intImage))
        item.setText(arrayOf(name, "Integer", value.valueToString()))
    }

    private fun loadLong(name: String, item: TreeItem, value: LongTag) {
        item.setImage(arrayOf(longImage))
        item.setText(arrayOf(name, "Long", value.valueToString()))
    }

    private fun loadFloat(name: String, item: TreeItem, value: FloatTag) {
        item.setImage(arrayOf(floatImage))
        item.setText(arrayOf(name, "Float", value.valueToString()))
    }

    private fun loadDouble(name: String, item: TreeItem, value: DoubleTag) {
        item.setImage(arrayOf(doubleImage))
        item.setText(arrayOf(name, "Double", value.valueToString()))
    }

    private fun loadString(name: String, item: TreeItem, value: StringTag) {
        item.setImage(arrayOf(textImage))
        item.setText(arrayOf(name, "String / Text", value.valueToString()))
    }

    private fun loadList(name: String, item: TreeItem) {
        item.setImage(arrayOf(listImage))
        item.setText(arrayOf(name, "List", ""))
        item.setData("ShouldGenerateSubItems", true)
    }

    private fun loadCompound(name: String, item: TreeItem) {
        item.setImage(arrayOf(compoundImage))
        item.setText(arrayOf(name, "Compound", ""))
        item.setData("ShouldGenerateSubItems", true)
    }
}
