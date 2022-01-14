package com.ultreon.explorers

import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.events.TreeAdapter
import org.eclipse.swt.events.TreeEvent
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.graphics.ImageLoader
import org.eclipse.swt.graphics.Point
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.widgets.*
import java.io.File
import java.io.IOException
import java.util.*


/**
 * Main kotlin file.
 *
 * @author Qboi123
 */
abstract class ExplorerWindow(file: File?) {
    protected var rootMenu: Menu
        private set

    // File management
    private var openMenuItem: MenuItem
    private var saveMenuItem: MenuItem
    private var saveAsMenuItem: MenuItem
    private var exitMenuItem: MenuItem

    private var openDialog: FileDialog
    private var saveDialog: FileDialog

    var isChanged: Boolean = false
        protected set
    var saveLocation: File? = null
        private set

    // Components
    protected var display: Display
    protected var shell: Shell
    protected var tree: Tree

    // Columns
    protected var nameCol: TreeColumn

    // Images
    var unknownImage: Image
        private set
    var folderImage: Image
        private set
    var packageImage: Image
        private set
    var compoundImage: Image
        private set
    var listImage: Image
        private set
    var byteImage: Image
        private set
    var shortImage: Image
        private set
    var intImage: Image
        private set
    var longImage: Image
        private set
    var doubleImage: Image
        private set
    var floatImage: Image
        private set
    var byteArrayImage: Image
        private set
    var shortArrayImage: Image
        private set
    var intArrayImage: Image
        private set
    var longArrayImage: Image
        private set
    var floatArrayImage: Image
        private set
    var doubleArrayImage: Image
        private set
    var textImage: Image
        private set

    /**
     * Initialization
     */
    init {
        instance = this

        // Prepare.
        display = Display()

        // Images
//        unknownImage = Image(display, ImageLoader().load("ObjectTypes/Unknown.png")[0])
//        folderImage = Image(display, ImageLoader().load("ObjectTypes/Folder.png")[0])
//        packageImage = Image(display, ImageLoader().load("ObjectTypes/Package.png")[0])
//        compoundImage = Image(display, ImageLoader().load("ObjectTypes/Compound.png")[0])
//        listImage = Image(display, ImageLoader().load("ObjectTypes/List.png")[0])
//        byteImage = Image(display, ImageLoader().load("ObjectTypes/Byte.png")[0])
//        shortImage = Image(display, ImageLoader().load("ObjectTypes/Short.png")[0])
//        intImage = Image(display, ImageLoader().load("ObjectTypes/Integer.png")[0])
//        longImage = Image(display, ImageLoader().load("ObjectTypes/Long.png")[0])
//        doubleImage = Image(display, ImageLoader().load("ObjectTypes/Double.png")[0])
//        floatImage = Image(display, ImageLoader().load("ObjectTypes/Float.png")[0])
//        byteArrayImage = Image(display, ImageLoader().load("ObjectTypes/ByteArray.png")[0])
//        shortArrayImage = Image(display, ImageLoader().load("ObjectTypes/ShortArray.png")[0])
//        intArrayImage = Image(display, ImageLoader().load("ObjectTypes/IntegerArray.png")[0])
//        longArrayImage = Image(display, ImageLoader().load("ObjectTypes/LongArray.png")[0])
//        floatArrayImage = Image(display, ImageLoader().load("ObjectTypes/FloatArray.png")[0])
//        doubleArrayImage = Image(display, ImageLoader().load("ObjectTypes/DoubleArray.png")[0])
//        textImage = Image(display, ImageLoader().load("ObjectTypes/Text.png")[0])

        unknownImage = loadImage("ObjectTypes/Unknown.png")
        folderImage = loadImage("ObjectTypes/Folder.png")
        packageImage = loadImage("ObjectTypes/Package.png")
        compoundImage = loadImage("ObjectTypes/Compound.png")
        listImage = loadImage("ObjectTypes/List.png")
        byteImage = loadImage("ObjectTypes/Byte.png")
        shortImage = loadImage("ObjectTypes/Short.png")
        intImage = loadImage("ObjectTypes/Integer.png")
        longImage = loadImage("ObjectTypes/Long.png")
        doubleImage = loadImage("ObjectTypes/Double.png")
        floatImage = loadImage("ObjectTypes/Float.png")
        byteArrayImage = loadImage("ObjectTypes/ByteArray.png")
        shortArrayImage = loadImage("ObjectTypes/ShortArray.png")
        intArrayImage = loadImage("ObjectTypes/IntegerArray.png")
        longArrayImage = loadImage("ObjectTypes/LongArray.png")
        floatArrayImage = loadImage("ObjectTypes/FloatArray.png")
        doubleArrayImage = loadImage("ObjectTypes/DoubleArray.png")
        textImage = loadImage("ObjectTypes/Text.png")

        // Shell
        shell = Shell(display)
        shell.text = "Generic Explorer Window"
        shell.layout = FillLayout()
        shell.size = Point(450, 600)

        // Dialogs
        openDialog = FileDialog(shell, SWT.OPEN)
        openDialog.filterNames = arrayOf(".dat Files", ".nbt Files", ".mca Files", "All Files (*.*)")
        openDialog.filterExtensions = arrayOf("*.dat", "*.nbt", "*.mca", "*.*") // Windows
        openDialog.fileName = "fred.bat"

        saveDialog = FileDialog(shell, SWT.SAVE)
        saveDialog.filterNames = arrayOf(".dat Files", ".nbt Files", ".mca Files", "All Files (*.*)")
        saveDialog.filterExtensions = arrayOf("*.dat", "*.nbt", "*.mca", "*.*") // Windows
        saveDialog.fileName = "fred.bat"

        // Menus
        rootMenu = Menu(shell, SWT.BAR)

        val fileMenuItem = MenuItem(rootMenu, SWT.CASCADE)
        fileMenuItem.text = "File"
        val fileMenu = Menu(shell, SWT.DROP_DOWN)
        fileMenuItem.menu = fileMenu

        openMenuItem = MenuItem(fileMenu, SWT.PUSH)
        openMenuItem.text = "&New"
        openMenuItem.setData("OnWidgetSelected", Runnable(::new))
        openMenuItem.addSelectionListener(MenuBarEventHandlers())

        openMenuItem = MenuItem(fileMenu, SWT.PUSH)
        openMenuItem.text = "&Open..."
        openMenuItem.setData("OnWidgetSelected", Runnable(::open))
        openMenuItem.addSelectionListener(MenuBarEventHandlers())

        saveMenuItem = MenuItem(fileMenu, SWT.PUSH)
        saveMenuItem.text = "&Save"
        saveMenuItem.setData("OnWidgetSelected", Runnable(::save))
        saveMenuItem.addSelectionListener(MenuBarEventHandlers())

        saveAsMenuItem = MenuItem(fileMenu, SWT.PUSH)
        saveAsMenuItem.text = "Save &As..."
        saveAsMenuItem.setData("OnWidgetSelected", Runnable(::saveAs))
        saveAsMenuItem.addSelectionListener(MenuBarEventHandlers())

        exitMenuItem = MenuItem(fileMenu, SWT.PUSH)
        exitMenuItem.text = "E&xit"
        exitMenuItem.setData("OnWidgetSelected", Runnable(::exit))
        exitMenuItem.addSelectionListener(MenuBarEventHandlers())

        // Set window menu bar.
        shell.menuBar = rootMenu

        // Initiate tree
        tree = Tree(shell, SWT.BORDER or SWT.H_SCROLL or SWT.V_SCROLL or SWT.FULL_SELECTION)
        tree.headerVisible = true

        // Create columns
        nameCol = TreeColumn(tree, SWT.LEFT)
        nameCol.text = "Name"
        nameCol.width = 200
        nameCol.moveable = false
        // Initialize items
        this.addTreeItems()

        // Add listeners
        tree.addTreeListener(TreeEventHandlers(this::generateSubItems))

        try {
            file?.let { open(it) }
            if (file == null) {
                new()
            }
        } catch (e: IOException) {
            val messageBox = MessageBox(shell, SWT.ICON_ERROR or SWT.OK)
            messageBox.text = "I/O Error"
            messageBox.message = e.localizedMessage
        }
    }

    fun loadMultiResIcon(path: String): Array<Image> {
        return Arrays.stream(ImageLoader().load(this::class.java.getResourceAsStream("/$path"))).map {
            Image(display, it)
        }.toList().toTypedArray()
    }

    fun loadImage(path: String): Image {
        return Image(display, ImageLoader().load(this::class.java.getResourceAsStream("/$path"))[0])
    }

    private fun new() {
        if (checkForUnsavedChanged()) {
            reset()
            onNew()
        }
    }

    /**
     * Exits the program.
     */
    private fun exit() {
        if (checkForUnsavedChanged()) {
            shell.close()
        }
    }

    /**
     * Request to open a new file.
     */
    fun open() {
        if (checkForUnsavedChanged()) {
            reset()
            val ret: String = openDialog.open() ?: return
            val loc = File(ret)
            saveLocation = loc
            onOpen(loc)
        }
    }

    /**
     * Request to open a new file.
     */
    fun open(file: File) {
        if (checkForUnsavedChanged()) {
            reset()
            saveLocation = file
            onOpen(file)
        }
    }

    /**
     * Checks for unsaved changes, and returns true if the user accepted the exit.
     *
     * @return true if the user didn't cancel it.
     */
    private fun checkForUnsavedChanged(): Boolean {
        if (isChanged) {
            val messageBox = MessageBox(shell, SWT.ICON_WARNING or SWT.YES or SWT.NO or SWT.CANCEL)

            messageBox.text = "Warning"
            messageBox.message = "There are unsaved changes.\nDo you want to save them?"
            val buttonID = messageBox.open()
            when (buttonID) {
                SWT.YES -> {
                    save()
                    reset()
                    return true
                }
                SWT.NO -> {
                    reset()
                    return true
                }
                SWT.CANCEL -> {
                    return false
                }
            }
            println(buttonID)
        }
        return true
    }

    abstract fun onOpen(file: File)

    /**
     * Save file, request file location if there's not saved before.
     */
    fun save() {
        if (saveLocation != null) {
            onSave()
        } else {
            saveAs()
        }
    }

    /**
     * Save as a new location.
     */
    private fun saveAs() {
        saveDialog.open()?.let {
            val file = File(it)
            saveLocation = file
            onSave()
        }
    }

    /**
     * Save event handler, here the file(s) will be saved.
     */
    protected abstract fun onSave()

    /**
     * Discards Changes.
     */
    private fun reset() {
        tree.removeAll()
        tree.clearAll(true)
        saveLocation = null
        onReset()
    }

    abstract fun onReset()

    /**
     * Generate sub-items of the tree.
     */
    protected abstract fun generateSubItems(item: TreeItem)

    /**
     * Post initialization.
     */
    fun postInit() {
        shell.pack()
        shell.size = Point(600, 450)
        shell.open()
        while (!shell.isDisposed) {
            if (!display.readAndDispatch()) {
                display.sleep()
            }
        }
        display.dispose()
    }

    /**
     * Companion object.
     */
    companion object {
        /**
         * Explorer Window abstract class instance.
         */
        lateinit var instance: ExplorerWindow
            private set
    }

    /**
     * Menu bar listener
     */
    class MenuBarEventHandlers : SelectionAdapter() {
        override fun widgetSelected(event: SelectionEvent) {
            // Run the event handler from the widget through the data if the widget.
            val onWidgetSelectedHandler = event.widget.getData("OnWidgetSelected")
            if (onWidgetSelectedHandler is Runnable) {
                onWidgetSelectedHandler.run()
            }
        }
    }

    private class TreeEventHandlers(private val generateSubItems: (TreeItem) -> Unit) : TreeAdapter() {
        override fun treeCollapsed(e: TreeEvent) {
            val treeItem = e.item as TreeItem
            val items = treeItem.items
            for (item in items) {
                // Check if the current item should generate sub-items.
                val shouldGenerateSubItems = item.getData("ShouldGenerateSubItems") as Boolean?
                if (shouldGenerateSubItems != null && shouldGenerateSubItems) {
                    item.expanded = false
                    item.removeAll()
                }
            }
        }

        override fun treeExpanded(e: TreeEvent) {
            val treeItem = e.item as TreeItem
            val items = treeItem.items
            for (item in items) {
                // Check if the current item should generate sub-items.
                val shouldGenerateSubItems = item.getData("ShouldGenerateSubItems") as Boolean?
                if (shouldGenerateSubItems != null && shouldGenerateSubItems) {
                    generateSubItems(item)
                }
            }
        }
    }

    abstract fun addTreeItems()
    abstract fun onNew()
}