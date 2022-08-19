package com.example.smartlockerktx

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.IntegerRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.smartlockerktx.common.*
import com.example.smartlockerktx.database.model.Employee
import com.example.smartlockerktx.viewmodel.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.Inet4Address


class MainActivity : AppCompatActivity() {

    var curUserPincode = ""
    var isLogined = false
    var userLogined = ""
    lateinit var buttons: Array<Array<Button>>
    lateinit var viewModel : SharedViewModel
    private lateinit var btnPinCode: ImageButton
    private lateinit var btnLogin: ImageButton
    private lateinit var btnHome: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var btnBack: ImageButton
    private var dlgPinCode: Dialog? = null
    var dlgLogin: Dialog? = null
    var isEditUsername = true
    private var dialog: Dialog? = null
    private var employees = ArrayList<Employee>()
    var uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN)

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = 5894
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        init()
        viewModel.startUp(application)
        observeViewModel()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun observeViewModel() {
        viewModel.lockers.observe(this, Observer { lockers ->
//            buttons = arrayOf<Array<Button>>()
            lockers.forEach { it ->
                val row = it.row
                val col = it.column
                buttons[row-1][col-1].text = it.label
                if (it.available == 0) {
                    if (it.doorState == "OPEN") {
                        buttons[row - 1][col - 1].backgroundTintList =
                            getColorStateList(R.color.opened)
                    }
                    buttons[row - 1][col - 1].backgroundTintList =
                        getColorStateList(R.color.available)
                }
                else if (it.available == 1) {
                    if (it.doorState == "OPEN") {
                        buttons[row - 1][col - 1].backgroundTintList =
                            getColorStateList(R.color.opened)
                    }
                    buttons[row - 1][col - 1].backgroundTintList = getColorStateList(R.color.unavailable)
                    buttons[row - 1][col - 1].stateDescription = 3.toString()
                }
                else
                    buttons[row-1][col-1].backgroundTintList = getColorStateList(R.color.warning)
                buttons[row-1][col-1].isEnabled = it.available == 1
                buttons[row-1][col-1].contentDescription = it.lockerId
                buttons[row-1][col-1].setOnClickListener { btn ->
                    showPinCodeDialog(btn)
                }

            }
        })
        viewModel.employees.observe(this, Observer {
            employees = it as ArrayList<Employee>
            Log.d("Abcd", employees.size.toString())
        })
        viewModel.employeeLocker.observe(this, Observer {lockers ->
            lockers.forEach {
                val row = it.row
                val col = it.column
                buttons[row-1][col-1].text = it.label
                if (it.available == 0)
                    buttons[row-1][col-1].backgroundTintList = getColorStateList(R.color.can_use)
                else
                    buttons[row-1][col-1].backgroundTintList = getColorStateList(R.color.unavailable)
                buttons[row-1][col-1].isEnabled = true
                buttons[row-1][col-1].setOnClickListener{ btn ->
                    if(it.available != 0 ){
                        if(isLogined) {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("Thông báo")
                            builder.setMessage("Tủ đang được sử dụng, vui lòng chọn ô tủ khác")
                            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                                dialog.dismiss()
                            }
                            dialog?.dismiss()
                            dialog = builder.show()
                        }
                        else showPinCodeDialog(btn)
                    }
                    else showPinCodeDialog(btn)
                }
            }
        })
    }
    @RequiresApi(Build.VERSION_CODES.R)
    private fun showPinCodeDialog(view : View) {
        if (dlgPinCode != null && dlgPinCode!!.isShowing) {
            dlgPinCode!!.dismiss()
        }
        dlgPinCode = Dialog(this)
        dlgPinCode!!.setContentView(R.layout.dialog_pincode_input as Int)
        dlgPinCode!!.show()
        val tvTitle = dlgPinCode!!.findViewById<View>(R.id.tvTitle) as TextView
        tvTitle.textSize = 18.0f
        val editPincode = dlgPinCode!!.findViewById<View>(R.id.editLabel) as EditText
        editPincode.textSize = 24.0f
        (dlgPinCode!!.findViewById<View>(R.id.btnOpen) as Button).setOnClickListener {
            val sPinCode = editPincode.text.toString()
            if(isLogined){
                if (sPinCode != curUserPincode) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Thông báo")
                    builder.setMessage("Mã PIN không hợp lệ\n" +
                            "\n" +
                            "Incorrect PINCODE")

                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                    }

                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
                        dialog.dismiss()
                        this.dlgPinCode!!.dismiss()
                        curUserPincode = ""
                        viewModel.displayLocker()

                    }
                    dialog?.dismiss()
                    dialog = builder.show()
                }
                else{
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Thông báo")
                    builder.setMessage("Mở tủ thành công")
                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        dialog.dismiss()
                        viewModel.updateAccess(view.contentDescription.toString(), curDateTime(), "OCCUPIED", userLogined)
                        lockedEmployee += userLogined
                        userLogined = ""
                        this.dlgPinCode!!.dismiss()
                        view.stateDescription = 3.toString()
                        curUserPincode = ""
                        isLogined = false
                        viewModel.updateLockerStatus(view.contentDescription.toString(), 1)

                    }
                    dialog?.dismiss()
                    dialog = builder.show()
                }
            }
            else{
                var errorRemain = Integer.parseInt(view.stateDescription as String)
                viewModel.getLockerUser(view.contentDescription.toString())
                viewModel.lockerUser.observe(this) {
                    Log.d("Test", "Run")
                    if (sPinCode != it.pinCode) {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Thông báo")
                        if (errorRemain >0 ) {
                            builder.setMessage(
                                "Mã PIN không hợp lệ. \n" +
                                        "\n" +
                                        "Incorrect PINCODE"
                            )
                            view.stateDescription = (errorRemain--).toString()
                            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                            }
                            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                                dialog.dismiss()
                                this.dlgPinCode!!.dismiss()
                                viewModel.displayLocker()
                            }
                        }
                        else{
                            // update dirty
                            builder.setMessage(
                                "Nhập sai quá 3 lần. Vui lòng liên hệ quản lý \n"
                            )
//                            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
//                            }
                            viewModel.updateLockerStatus(view.contentDescription.toString(), 2)
                            viewModel.updateAccess(
                                view.contentDescription.toString(),
                                curDateTime(),
                                "SUSPEND"
                            )
                            builder.setNegativeButton(android.R.string.yes) { dialog, which ->
                                dialog.dismiss()
                                this.dlgPinCode!!.dismiss()
                                viewModel.displayLocker()
                            }
                        }

                        dialog?.dismiss()
                        dialog = builder.show()
                    } else {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Thông báo")
                        builder.setMessage("Mở tủ thành công\nBạn có muốn tiếp tục sử dụng tủ ?")
                        builder.setPositiveButton("Trả tủ") { dialog, which ->
                            viewModel.updateAccess(
                                view.contentDescription.toString(),
                                curDateTime(),
                                "FREE",
                                ""
                            )
                            view.stateDescription = ""
                            dialog.dismiss()
                            this.dlgPinCode!!.dismiss()
                            Log.d("AAAAA", view.contentDescription.toString())
                            viewModel.updateLockerStatus(view.contentDescription.toString(), 0)
                        }
                        builder.setNegativeButton("Tiếp tục") { dialog, which ->

                            viewModel.updateAccess(
                                view.contentDescription.toString(),
                                curDateTime(),
                                "OCCUPIED"
                            )
                            dialog.dismiss()
                            this.dlgPinCode!!.dismiss()
                        }
                        dialog?.dismiss()
                        dialog = builder.show()
                    }
                }
            }
        }
        (dlgPinCode!!.findViewById<View>(R.id.btnCancel) as Button).setOnClickListener { dlgPinCode!!.dismiss() }
        dlgPinCode!!.findViewById<View>(R.id.btnNum0).setOnClickListener {
            editPincode.append(
                "0"
            )
        }
        dlgPinCode!!.findViewById<View>(R.id.btnNum1).setOnClickListener {
            editPincode.append(
                "1"
            )
        }
        dlgPinCode!!.findViewById<View>(R.id.btnNum2).setOnClickListener {
            editPincode.append(
                "2"
            )
        }
        dlgPinCode!!.findViewById<View>(R.id.btnNum3).setOnClickListener {
            editPincode.append(
                "3"
            )
        }
        dlgPinCode!!.findViewById<View>(R.id.btnNum4).setOnClickListener {
            editPincode.append(
                "4"
            )
        }
        dlgPinCode!!.findViewById<View>(R.id.btnNum5).setOnClickListener {
            editPincode.append(
                "5"
            )
        }
        dlgPinCode!!.findViewById<View>(R.id.btnNum6).setOnClickListener {
            editPincode.append(
                "6"
            )
        }
        dlgPinCode!!.findViewById<View>(R.id.btnNum7).setOnClickListener {
            editPincode.append(
                "7"
            )
        }
        dlgPinCode!!.findViewById<View>(R.id.btnNum8).setOnClickListener {
            editPincode.append(
                "8"
            )
        }
        dlgPinCode!!.findViewById<View>(R.id.btnNum9).setOnClickListener {
            editPincode.append(
                "9"
            )
        }
        dlgPinCode!!.findViewById<View>(R.id.btnBackSpace).setOnClickListener {
            if (editPincode.text.toString().isNotEmpty()) {
                val sub = editPincode.text.toString().substring(
                    0,
                    editPincode.text.toString().length - 1
                )
                editPincode.setText(sub)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun showLoginDialog() {
        if (dlgLogin != null && dlgLogin!!.isShowing) {
            dlgLogin!!.dismiss()
        }
        dlgLogin = Dialog(this)
        dlgLogin!!.window!!.setFlags(8, 8)
        dlgLogin!!.window!!.addFlags(131200)
        dlgLogin!!.window!!.decorView.systemUiVisibility = 5894
        dlgLogin!!.setContentView(R.layout.dialog_login as Int)
        dlgLogin!!.show()
        dlgLogin!!.window!!.clearFlags(8)
        val tvTitle = dlgLogin!!.findViewById<View>(R.id.tvTitle) as TextView
        tvTitle.textSize = 18.0f
        tvTitle.setTypeface(Typeface.DEFAULT_BOLD)
        val editPassword = dlgLogin!!.findViewById<View>(R.id.password) as EditText
        val editUseName = dlgLogin!!.findViewById<View>(R.id.username) as EditText
        editPassword.textSize = 24.0f
        editUseName.textSize = 24.0f


        editPassword.setOnTouchListener { v, event ->
            isEditUsername = false
            Log.d("onClick", "PW Click")
            false
        }
        editUseName.setOnTouchListener { v, event ->
            isEditUsername = true
            Log.d("onClick", "Ur Click")
            false
        }
        (dlgLogin!!.findViewById<View>(R.id.btnlogin) as Button).setOnClickListener {
                for( i in 0 until employees.size) {
                    if(employees[i].username.equals(editUseName.text.toString()) &&
                        employees[i].password.equals(editPassword.text.toString())){
                        curUserPincode = employees[i].pinCode.toString()
                        Log.d("Abcd", "gotcha!!")

                        userLogined = employees[i].employeeId
                        Log.d("Array", lockedEmployee.size.toString())
                        if (lockedEmployee.contains(userLogined)){
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("Thông báo")
                            builder.setMessage("Bạn đã sử dụng một ô tủ")
                            builder.setPositiveButton("Oke") { dialog, which ->
                                dialog.dismiss()
                            }
                            dialog?.dismiss()
                            dialog = builder.show()
                        }
                        else{
                            viewModel.getEmployeeLocker(employees[i].groupEmployeeId)
                            isLogined = true
                        }
                        dlgLogin!!.dismiss()
                        break
                    }
            }
        }
        (dlgLogin!!.findViewById<View>(R.id.btnCancel) as Button).setOnClickListener { dlgLogin!!.dismiss() }
        dlgLogin!!.findViewById<View>(R.id.btnNum0).setOnClickListener {
            if (isEditUsername) {
                editUseName.append("0")
            } else editPassword.append("0")
        }
        dlgLogin!!.findViewById<View>(R.id.btnNum1).setOnClickListener {
            if (isEditUsername) {
                editUseName.append("1")
            } else editPassword.append("1")
        }
        dlgLogin!!.findViewById<View>(R.id.btnNum2).setOnClickListener {
            if (isEditUsername) {
                editUseName.append("2")
            } else editPassword.append("2")
        }
        dlgLogin!!.findViewById<View>(R.id.btnNum3).setOnClickListener {
            if (isEditUsername) {
                editUseName.append("3")
            } else editPassword.append("3")
        }
        dlgLogin!!.findViewById<View>(R.id.btnNum4).setOnClickListener {
            if (isEditUsername) {
                editUseName.append("4")
            } else editPassword.append("4")
        }
        dlgLogin!!.findViewById<View>(R.id.btnNum5).setOnClickListener {
            if (isEditUsername) {
                editUseName.append("5")
            } else editPassword.append("5")
        }
        dlgLogin!!.findViewById<View>(R.id.btnNum6).setOnClickListener {
            if (isEditUsername) {
                editUseName.append("6")
            } else editPassword.append("6")
        }
        dlgLogin!!.findViewById<View>(R.id.btnNum7).setOnClickListener {
            if (isEditUsername) {
                editUseName.append("7")
            } else editPassword.append("7")
        }
        dlgLogin!!.findViewById<View>(R.id.btnNum8).setOnClickListener {
            if (isEditUsername) {
                editUseName.append("8")
            } else editPassword.append("8")
        }
        dlgLogin!!.findViewById<View>(R.id.btnNum9).setOnClickListener {
            if (isEditUsername) {
                editUseName.append("9")
            } else editPassword.append("9")
        }
        dlgLogin!!.findViewById<View>(R.id.btnA).setOnClickListener {
            if (isEditUsername) {
                editUseName.append("a")
            } else editPassword.append("a")
        }
        dlgLogin!!.findViewById<View>(R.id.btnB).setOnClickListener {
            if (isEditUsername) {
                editUseName.append("b")
            } else editPassword.append("b")
        }
        dlgLogin!!.findViewById<View>(R.id.btnC).setOnClickListener {
            if (isEditUsername) {
                editUseName.append("c")
            } else editPassword.append("c")
        }
        dlgLogin!!.findViewById<View>(R.id.btnD).setOnClickListener {
            if (isEditUsername) {
                editUseName.append("d")
            } else editPassword.append("d")
        }
        dlgLogin!!.findViewById<View>(R.id.btnE).setOnClickListener {
            if (isEditUsername) {
                editUseName.append("e")
            } else editPassword.append("e")
        }
        dlgLogin!!.findViewById<View>(R.id.btnF).setOnClickListener {
            if (isEditUsername) {
                editUseName.append("f")
            } else editPassword.append("f")
        }
        dlgLogin!!.findViewById<View>(R.id.btnBackSpace).setOnClickListener {
            if (isEditUsername && editUseName.text.toString().isNotEmpty()) {
                val sub = editUseName.text.toString().substring(
                    0,
                    editUseName.text.toString().length - 1
                )
                editUseName.setText(sub)
            } else if (!isEditUsername && editPassword.text.toString().isNotEmpty()) {
                val sub = editPassword.text.toString().substring(
                    0,
                    editPassword.text.toString().length - 1
                )
                editPassword.setText(sub)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    private fun init(){
        btnPinCode = findViewById<ImageButton>(R.id.btnPinCode)
        btnLogin = findViewById<ImageButton>(R.id.login)
        btnLogin.setOnClickListener { showLoginDialog() }
        buttons = arrayOf<Array<Button>>()
        btnHome = findViewById<ImageButton>(R.id.btnDone)
        btnHome.setOnClickListener { viewModel.displayLocker() }
        btnNext = findViewById(R.id.btnNext)
        btnBack = findViewById(R.id.btnBack)
        btnBack.isEnabled = false
        btnNext.setOnClickListener {
            btnBack.isEnabled = true
            btnNext.isEnabled = false
            cur_page = 1
            findViewById<TextView>(R.id.tvPage).text = "2/2"
            resetBtn()
            viewModel.displayLocker()
        }

        btnBack.setOnClickListener {
            btnBack.isEnabled = false
            btnNext.isEnabled = true
            cur_page = 0
            findViewById<TextView>(R.id.tvPage).text = "1/2"
            resetBtn()
            viewModel.displayLocker()
        }

        resetBtn()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun resetBtn(){
        for (i in 0..2) {
            var arrButtons = arrayOf<Button>()
            for (j in 0..6) {
                val button = findViewById<View>(
                    resources.getIdentifier(
                        "btnLocker" + (i+1).toString() + (j+1).toString(),
                        "id",
                        BuildConfig.APPLICATION_ID
                    )
                ) as Button
                button.isEnabled = false
                button.text = ""
//                button.textSize = 20.0f
                button.setTextColor(-1)
                button.backgroundTintList = getColorStateList(R.color.blank)
                arrButtons  +=  button
            }
            buttons += arrButtons
        }
    }
}