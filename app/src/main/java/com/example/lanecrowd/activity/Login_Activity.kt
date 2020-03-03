package com.example.lanecrowd.activityimport android.app.Activityimport android.content.Contextimport android.content.Intentimport android.net.ConnectivityManagerimport android.os.Bundleimport android.util.Patternsimport android.view.Viewimport android.view.WindowManagerimport android.view.animation.AnimationUtilsimport android.view.inputmethod.InputMethodManagerimport android.widget.*import androidx.appcompat.app.AppCompatActivityimport androidx.core.content.ContextCompatimport androidx.lifecycle.Observerimport androidx.lifecycle.ViewModelProviderimport androidx.lifecycle.ViewModelProvidersimport com.airbnb.lottie.LottieAnimationViewimport com.example.lancrowd.activity.modal.RegisterResModalimport com.example.lanecrowd.Rimport com.example.lanecrowd.Session_Package.SessionManagerimport com.example.lanecrowd.view_modal.LoginRegUserVMimport com.google.android.material.dialog.MaterialAlertDialogBuilderimport com.google.android.material.snackbar.Snackbarimport com.google.android.material.textfield.TextInputEditTextimport com.jakewharton.rxbinding2.view.RxViewimport io.reactivex.Observableimport io.reactivex.android.schedulers.AndroidSchedulersimport io.reactivex.disposables.CompositeDisposableimport io.reactivex.disposables.Disposableimport java.util.concurrent.TimeUnitclass Login_Activity : AppCompatActivity(), View.OnClickListener {    private var loginRegObservable: Disposable?=null    var compositeDisposable:CompositeDisposable =  CompositeDisposable();    private var isMobile: Boolean=false    var login_sign_up: TextView? = null    var register_signin: TextView? = null    var forget_password: TextView? = null    var register_layout: LinearLayout? = null    var login_layout: LinearLayout? = null    var login_sign_inbtn: TextView? = null    var register_sign_upbtn: TextView? = null    var loading_anim: LottieAnimationView? = null    var loading_animLogin: LottieAnimationView? = null    var loginEmail: EditText? = null    var loginPassword: TextInputEditText? = null    var reg_name: EditText? = null    var reg_email: EditText? = null    var reg_password: TextInputEditText? = null    var reg_re_password: TextInputEditText? = null    internal var showPassword: Boolean? = false    lateinit var viewmodel: LoginRegUserVM;    private var session: SessionManager? = null    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        setContentView(R.layout.login_main)        initViews()    }    private fun initViews() {        viewmodel = ViewModelProvider(this).get(LoginRegUserVM::class.java!!)        session = SessionManager(applicationContext)        //check user is logged in or not if yes then it will redirect to HomeActivity        session!!.checkLogin()        login_sign_up = findViewById(R.id.login_sign_up)        register_signin = findViewById(R.id.register_signin)        forget_password = findViewById(R.id.forget_password)        register_layout = findViewById(R.id.register_layout)        login_layout = findViewById(R.id.login_layout)        //login and register button        login_sign_inbtn = findViewById(R.id.login_sign_in)        register_sign_upbtn = findViewById(R.id.register_sign_up)        loading_anim = findViewById(R.id.loading_anim)        loading_animLogin = findViewById(R.id.loading_animLogin)        //fields of sign in        loginEmail = findViewById(R.id.email)        loginPassword = findViewById(R.id.password)        //fields of register        reg_name = findViewById(R.id.reg_name)        reg_email = findViewById(R.id.reg_email)        reg_password = findViewById(R.id.reg_password)        reg_re_password = findViewById(R.id.reg_re_password)        //set listener        forget_password!!.setOnClickListener(this)        register_signin!!.setOnClickListener(this)        //set login and register button listener          setLoginRegisterButtonListener()    }    private fun setLoginRegisterButtonListener() {        val observable1 = RxView.clicks(login_sign_inbtn!!).map { o -> login_sign_inbtn!! }        val observable2 = RxView.clicks(register_sign_upbtn!!).map { o -> register_sign_upbtn!! }        loginRegObservable = Observable.merge(observable1, observable2).throttleFirst(2, TimeUnit.SECONDS)                .observeOn(AndroidSchedulers.mainThread()).subscribe { o ->                    if(o==login_sign_inbtn)                    {                        checkLoginValidation()                        //gotoHomeActivtiy()                    }                    else if(o==register_sign_upbtn)                    {                        checkRegisterValidation()                    }                }        compositeDisposable.add(loginRegObservable!!)    }    override fun onClick(v: View) {        when (v.id) {            R.id.login_sign_up -> {                hideSoftKeyBoard()                showRegisterForm()            }            R.id.register_signin -> {                hideSoftKeyBoard()                showSignInForm()            }            R.id.forget_password ->                startActivity(Intent(applicationContext, Forget_Pwd_Activity::class.java))        }    }    private fun visibleAnimation(value: Boolean) {        if(value) {            loading_anim!!.visibility = View.VISIBLE            loading_anim!!.playAnimation()        }        else {            loading_anim!!.visibility = View.GONE            loading_anim!!.pauseAnimation()        }    }    private fun visibleLoginAnimation(value: Boolean) {        if(value) {            loading_animLogin!!.visibility = View.VISIBLE            loading_animLogin!!.playAnimation()        }        else {            loading_animLogin!!.visibility = View.GONE            loading_animLogin!!.pauseAnimation()        }    }    private fun checkLoginValidation() {        hideKeyboardFrom(applicationContext!!, findViewById(R.id.viewGroup))        if (!validateLoginEmail())            return        if (!validateLoginPassword())            return        checkInternettoCallAPI(true)    }    private fun checkRegisterValidation() {        hideKeyboardFrom(applicationContext!!, findViewById(R.id.viewGroup))        if (!validateName())            return        if (!validateEmailorMobile())            return         if (!validatePassword())            return        if (!validateComfirmPassword())            return            checkInternettoCallAPI(false)        }    private fun checkInternettoCallAPI(value:Boolean) {        if (!isNetworkAvailable(applicationContext!!)) {          showSnackBar()        }else {            if(value)            {                visibleLoginAnimation(true)                callLgoinUserAPI()            }            else {                visibleAnimation(true)                callregisterUserAPI()            }        }    }    private fun showSnackBar() {        val snackbar = Snackbar.make(findViewById(R.id.viewGroup), "Please Check Your Internet Connection", Snackbar.LENGTH_SHORT)        snackbar.setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.red)        )        snackbar.setTextColor(ContextCompat.getColor(applicationContext, R.color.white)        )        snackbar.show()    }    fun hideKeyboardFrom(context: Context, view: View) {        try {            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager            imm.hideSoftInputFromWindow(view.windowToken, 0)        } catch (e: Exception) {        }    }    fun isNetworkAvailable(context: Context): Boolean {        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected    }    private fun callregisterUserAPI() {        try {            viewmodel.registerUser(reg_name!!.text.toString(),reg_email!!.text.toString(),reg_password!!.text.toString()).observe(this, Observer { resultPi ->                 visibleAnimation(false)                if(resultPi.status.equals("1"))                gotovarifyOTPActivity(resultPi)                else                {                    userIsAlreadyExist()                }            })        }catch (e:Exception)        {            e.printStackTrace()        }    }    private fun callLgoinUserAPI() {        try {            viewmodel.loginUser(loginEmail!!.text.toString(),loginPassword!!.text.toString()).observe(this, Observer { resultPi ->                 visibleLoginAnimation(false)                println("login_result"+resultPi)                val userdata: ArrayList<RegisterResModal.UserData> =resultPi.data                if(resultPi.status.equals("1")) {                    createUserSession(userdata)                    gotoHomeActivtiy()                }                else                    invalidEmailorPassword()            })        }catch (e:Exception)        {            e.printStackTrace()        }    }    private fun createUserSession(userdata: ArrayList<RegisterResModal.UserData>) {        //creating user's session        session!!.createLoginSession(userdata.get(0).user_id,                userdata.get(0).email,                userdata.get(0).phone,                userdata.get(0).full_name,                userdata.get(0).bio_graphy,                userdata.get(0).profile_picture,                userdata.get(0).cover_photo)    }    private fun invalidEmailorPassword() {        MaterialAlertDialogBuilder(this@Login_Activity,R.style.AlertDialogTheme)                .setTitle("LaneCrowd")                .setMessage("Email or Password is Incorrect")                .setPositiveButton("Ok") { dialogInterface, i -> }                .show()            }    private fun gotoHomeActivtiy() {        startActivity(Intent(applicationContext, HomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))    }    private fun userIsAlreadyExist() {        if(isMobile)            reg_email!!.error = "This Mobile is Already Exist!"        else            reg_email!!.error = "This Email is Already Exist!"        requestFocusCursor(reg_email!!)    }    private fun gotovarifyOTPActivity(resultPi: RegisterResModal?) {        startActivity(Intent(applicationContext,Verify_OTP_Activity::class.java).putExtra("result",resultPi))    }    private fun validateName(): Boolean {        if (reg_name!!.text!!.toString() == "") {            reg_name!!.error = "Please Enter Name"            requestFocusCursor(reg_name!!)            return false        } else if (reg_name!!.text!!.toString().length < 3) {            reg_name!!.error = "Please Enter Valid Name"            requestFocusCursor(reg_name!!)            return false        } else            reg_name!!.error = null        return true    }    private fun validateLoginEmail(): Boolean {        //input is mobile number        if (Character.isDigit(loginEmail!!.text.toString().get(0)) && !reg_email!!.text.toString().contains("@")) {            isMobile=true            if( Patterns.PHONE.matcher(loginEmail!!.text.toString()).matches())                return  true        }        else {            if (loginEmail!!.text!!.toString() == "") {                loginEmail!!.error = "Please Enter Email or Mobile"                requestFocusCursor(loginEmail!!)                return false            }            else if(Character.isDigit(loginEmail!!.text.toString().get(0))) {                loginEmail!!.error = "Please Enter Valid Email Address"                requestFocusCursor(loginEmail!!)                return false            }            else if (loginEmail!!.text.toString().length <= 12) {                loginEmail!!.error = "Please Enter Valid Email Address"                requestFocusCursor(loginEmail!!)                return false            } else if (!isValidEmail(loginEmail!!.text!!.toString())) {                loginEmail!!.error = "Please Enter Valid Email Address"                requestFocusCursor(loginEmail!!)                return false            } else {                isMobile = false                loginEmail!!.error = null            }        }        return true    }    private fun validateLoginPassword(): Boolean {        if (loginPassword!!.text!!.toString() == "") {            loginPassword!!.error = "Please Enter Password"            requestFocusCursor(loginPassword!!)            return false        } else if (loginPassword!!.text!!.toString().length < 5) {            loginPassword!!.error = "Please Enter Valid Password"            requestFocusCursor(loginPassword!!)            return false        } else            loginPassword!!.error = null        return true    }    private fun validateEmailorMobile(): Boolean {        //input is mobile number        if (Character.isDigit(reg_email!!.text.toString().get(0)) && !reg_email!!.text.toString().contains("@")) {            isMobile=true            if( Patterns.PHONE.matcher(reg_email!!.text.toString()).matches())                return  true        }        else {            if (reg_email!!.text!!.toString() == "") {                reg_email!!.error = "Please Enter Email or Mobile"                requestFocusCursor(reg_email!!)                return false            }            else if(Character.isDigit(reg_email!!.text.toString().get(0))) {                reg_email!!.error = "Please Enter Valid Email Address"                requestFocusCursor(reg_email!!)                return false            }            else if (reg_email!!.text.toString().length <= 12) {                reg_email!!.error = "Please Enter Valid Email Address"                requestFocusCursor(reg_email!!)                return false            } else if (!isValidEmail(reg_email!!.text!!.toString())) {                reg_email!!.error = "Please Enter Valid Email Address"                requestFocusCursor(reg_email!!)                return false            } else {                isMobile = false                reg_email!!.error = null            }        }        return true    }    private fun validatePassword(): Boolean {        if (reg_password!!.text!!.toString() == "") {            reg_password!!.error = "Please Enter Password"            requestFocusCursor(reg_password!!)            return false        } else if (reg_password!!.text!!.toString().length < 5) {            reg_password!!.error = "Please Enter Atleast 5 Digit Password"            requestFocusCursor(reg_password!!)            return false        } else            reg_password!!.error = null        return true    }    private fun validateComfirmPassword(): Boolean {        if (reg_re_password!!.text!!.toString() == "") {            reg_re_password!!.error = "Please Enter Comfirm Password"            requestFocusCursor(reg_re_password!!)            return false        } else if (!isSamePassword(reg_password!!.text!!.toString(), reg_re_password!!.text!!.toString())) {            reg_re_password!!.error = "Comfirm Password Do Not Matched"            requestFocusCursor(reg_re_password!!)            return false        } else            reg_re_password!!.error = null        return true    }    internal fun isSamePassword(password: String, comfirm_pass: String): Boolean {        return password == comfirm_pass    }    internal fun isValidEmail(email: String): Boolean {        return Patterns.EMAIL_ADDRESS.matcher(email).matches()    }    private fun requestFocusCursor(view: View) {        if (view.requestFocus()) {            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)        }    }    private fun showSignInForm() {        login_layout!!.requestLayout()        login_layout!!.visibility = View.VISIBLE        register_layout!!.visibility = View.GONE        val translate = AnimationUtils.loadAnimation(applicationContext, R.anim.translate_left_to_right)        login_layout!!.startAnimation(translate)        val clockwise = AnimationUtils.loadAnimation(applicationContext, R.anim.left_to_right)        register_signin!!.startAnimation(clockwise)    }    private fun showRegisterForm() {        register_layout!!.requestLayout()        login_layout!!.visibility = View.GONE        register_layout!!.visibility = View.VISIBLE        val translate = AnimationUtils.loadAnimation(applicationContext, R.anim.translate_right_to_left)        register_layout!!.startAnimation(translate)        val clockwise = AnimationUtils.loadAnimation(applicationContext, R.anim.left_to_right)        login_sign_up!!.startAnimation(clockwise)    }    private fun hideSoftKeyBoard() {        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager        try {            if (imm.isAcceptingText) {                // verify if the soft keyboard is open                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)            }        } catch (e: Exception) {        }    }    fun onClick_Customer(v: View) {        when (v.id) {            R.id.login_sign_up -> {                hideSoftKeyBoard()                showRegisterForm()            }            R.id.register_signin -> {                hideSoftKeyBoard()                showSignInForm()            }        }    }    override fun onBackPressed() {        super.onBackPressed()        finishAffinity()    }}