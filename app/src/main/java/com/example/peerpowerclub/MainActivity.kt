package com.example.peerpowerclub;


import android.app.AlertDialog
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peerpowerclub.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue

import com.google.firebase.ktx.Firebase
import com.payu.base.models.*
import com.payu.checkoutpro.PayUCheckoutPro
import com.payu.checkoutpro.models.PayUCheckoutProConfig
import com.payu.checkoutpro.utils.PayUCheckoutProConstants
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_HASH_NAME
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_HASH_STRING
import com.payu.ui.model.listeners.PayUCheckoutProListener
import com.payu.ui.model.listeners.PayUHashGenerationListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_si_details.*



 class MainActivity : AppCompatActivity() {

    private val email: String = "snooze@payu.in"
    private val phone = ""
    private val merchantName = "peerPowerClub"
    private val surl = "https://payuresponse.firebaseapp.com/success"
    private val furl = "https://payuresponse.firebaseapp.com/failure"
    private val amount = "200.0"
    var grplnk=""
    var userid=""
    var coursename=""
    //Test Key and Salt
    private val testKey = "IUIaFM"
    private val testSalt = "67njRYSI"
    private val merchantAccessKey = "E5ABOXOWAAZNXB6JEF5Z"
    private val merchantSecretKey = "e425e539233044146a2d185a346978794afd7c66"
    val database = Firebase.database


    //Prod Key and Salt
    private val prodKey = "CSQcrcoa"
    private val prodSalt = "FzOXL44o"

    private lateinit var binding: ActivityMainBinding

    // variable to track event time
    private var mLastClickTime: Long = 0
    private var reviewOrderAdapter: ReviewOrderRecyclerViewAdapter? = null
    private var billingCycle = arrayOf(
        "DAILY",
        "WEEKLY",
        "MONTHLY",
        "YEARLY",
        "ONCE",
        "ADHOC"
    )
    private var billingRule = arrayOf(
        "MAX",
        "EXACT"
    )

    private var billingLimit = arrayOf(
        "ON",
        "BEFORE",
        "AFTER"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        grplnk = intent.getStringExtra("grplnk").toString()
        userid = intent.getStringExtra("userid").toString()
        coursename = intent.getStringExtra("coursename").toString()
        grlnk.text = grplnk
        val myRef = database.getReference("users")
        myRef.child(userid).addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child("status").getValue<String>().equals("enrolled in" + coursename))

                    grlnk.visibility = View.VISIBLE
                else
                    grlnk.visibility = View.GONE

            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("hey", "Failed to read value.", error.toException())
            }

        })



        initializeSIView()
        setInitalData()
        initListeners()

    }

    private fun initializeSIView() {
        switch_si_on_off.setOnCheckedChangeListener { buttonView, isChecked -> if(isChecked)
        { layout_si_details.visibility = View.VISIBLE }
        else { layout_si_details.visibility = View.GONE }
        }

        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            billingCycle
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        et_billingCycle_value.adapter = adapter
        val billingRuleAdapter : ArrayAdapter<*> = ArrayAdapter<Any?>(
            this, android.R.layout.simple_spinner_item, billingRule
        )
        billingRuleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        et_billingRule_value.adapter = billingRuleAdapter

        val billingLimitAdapter : ArrayAdapter<*> = ArrayAdapter<Any?>(
            this, android.R.layout.simple_spinner_item, billingLimit
        )
        billingLimitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        et_billingLimit_value.adapter = billingLimitAdapter
    }

    private fun setInitalData() {
        updateProdEnvDetails()
        binding.etSurl.setText(surl)
        binding.etFurl.setText(furl)
        binding.etMerchantName.setText("peerPowerClub")
        binding.etPhone.setText(phone)
        binding.etAmount.setText(amount)
        binding.etUserCredential.setText("Apoorva")
        binding.etSurePayCount.setText("0")
    }

    private fun initListeners() {
        binding.radioGrpEnv.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            when (i) {
                R.id.radioBtnTest -> updateTestEnvDetails()
                R.id.radioBtnProduction -> updateProdEnvDetails()
                else -> updateTestEnvDetails()
            }
        }

        binding.switchEnableReviewOrder.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            if (b) showReviewOrderView() else hideReviewOrderView()
        }

        binding.btnAddItem.setOnClickListener { reviewOrderAdapter?.addRow() }
    }

    private fun hideReviewOrderView() {
        binding.rlReviewOrder.visibility = View.GONE
        reviewOrderAdapter = null
    }

    private fun showReviewOrderView() {
        binding.rlReviewOrder.visibility = View.VISIBLE
        reviewOrderAdapter = ReviewOrderRecyclerViewAdapter()
        binding.rvReviewOrder.layoutManager = LinearLayoutManager(this)
        binding.rvReviewOrder.adapter = reviewOrderAdapter
    }

    private fun updateTestEnvDetails() {
        //For testing
        binding.etKey.setText(testKey)
        binding.etSalt.setText(testSalt)
    }

    private fun updateProdEnvDetails() {
        //For Production
        binding.etKey.setText(prodKey)
        binding.etSalt.setText(prodSalt)
    }

    fun startPayment(view: View) {
        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()

        val paymentParams = preparePayUBizParams()
        initUiSdk(paymentParams)
    }

    fun preparePayUBizParams(): PayUPaymentParams {
        val vasForMobileSdkHash = HashGenerationUtils.generateHashFromSDK(
            "${binding.etKey.text}|${PayUCheckoutProConstants.CP_VAS_FOR_MOBILE_SDK}|${PayUCheckoutProConstants.CP_DEFAULT}|",
            binding.etSalt.text.toString()
        )
        val paymenRelatedDetailsHash = HashGenerationUtils.generateHashFromSDK(
            "${binding.etKey.text}|${PayUCheckoutProConstants.CP_PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK}|${binding.etUserCredential.text}|",
            binding.etSalt.text.toString()
        )

        val additionalParamsMap: HashMap<String, Any?> = HashMap()
        additionalParamsMap[PayUCheckoutProConstants.CP_UDF1] = "udf1"
        additionalParamsMap[PayUCheckoutProConstants.CP_UDF2] = "udf2"
        additionalParamsMap[PayUCheckoutProConstants.CP_UDF3] = "udf3"
        additionalParamsMap[PayUCheckoutProConstants.CP_UDF4] = "udf4"
        additionalParamsMap[PayUCheckoutProConstants.CP_UDF5] = "udf5"

        //Below params should be passed only when integrating Multi-currency support
        //TODO Please pass your own Merchant Access Key below as provided by your Key Account Manager at PayU.
        // Merchant Access Key used here is only for testing purpose.
//        additionalParamsMap[PayUCheckoutProConstants.CP_MERCHANT_ACCESS_KEY] = merchantAccessKey

        //Below hashes are static hashes and can be calculated and passed in additional params
        additionalParamsMap[PayUCheckoutProConstants.CP_VAS_FOR_MOBILE_SDK] = vasForMobileSdkHash
        additionalParamsMap[PayUCheckoutProConstants.CP_PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK] =
            paymenRelatedDetailsHash

        var siDetails: PayUSIParams? =null
        if(switch_si_on_off.isChecked) {
            siDetails  = PayUSIParams.Builder()
                .setIsFreeTrial(sp_free_trial.isChecked)
                .setBillingAmount(et_billingAmount_value.text.toString())
                .setBillingCycle(PayUBillingCycle.valueOf(et_billingCycle_value.selectedItem.toString()))
                .setBillingInterval(et_billingInterval_value.text.toString().toInt())
                .setPaymentStartDate(et_paymentStartDate_value.text.toString())
                .setPaymentEndDate(et_paymentEndDate_value.text.toString())
                .setRemarks(et_remarks_value.text.toString())
                .setBillingLimit(PayuBillingLimit.valueOf(et_billingLimit_value.selectedItem.toString()))
                .setBillingRule(PayuBillingRule.valueOf(et_billingRule_value.selectedItem.toString()))
                .build()
        }

        return PayUPaymentParams.Builder().setAmount(binding.etAmount.text.toString())
            .setIsProduction(binding.radioBtnProduction.isChecked)
            .setKey(binding.etKey.text.toString())
            .setProductInfo("Macbook Pro")
            .setPhone(binding.etPhone.text.toString())
            .setTransactionId(System.currentTimeMillis().toString())
            .setFirstName("Abc")
            .setEmail(email)
            .setSurl(binding.etSurl.text.toString())
            .setFurl(binding.etFurl.text.toString())
            .setUserCredential(binding.etUserCredential.text.toString())
            .setAdditionalParams(additionalParamsMap)
            .setPayUSIParams(siDetails)
            .build()
    }

    private fun initUiSdk(payUPaymentParams: PayUPaymentParams) {
        PayUCheckoutPro.open(
            this,
            payUPaymentParams,
            getCheckoutProConfig(),
            object : PayUCheckoutProListener {

                override fun onPaymentSuccess(response: Any) {

                    processResponse(response)
                }

                override fun onPaymentFailure(response: Any) {
                    processResponse(response)
                }

                override fun onPaymentCancel(isTxnInitiated: Boolean) {
                    showSnackBar(resources.getString(R.string.transaction_cancelled_by_user) + grplnk)
                    val Ref = database.getReference("users")
                    Ref.child(userid).child("status").setValue("enrolled in" + coursename)
                }

                override fun onError(errorResponse: ErrorResponse) {

                    val errorMessage: String
                    if (errorResponse != null && errorResponse.errorMessage != null && errorResponse.errorMessage!!.isNotEmpty())
                        errorMessage = errorResponse.errorMessage!!
                    else
                        errorMessage = resources.getString(R.string.some_error_occurred)
                    showSnackBar(errorMessage)


                }

                override fun generateHash(
                    map: HashMap<String, String?>,
                    hashGenerationListener: PayUHashGenerationListener
                ) {
                    if (map.containsKey(CP_HASH_STRING)
                        && map.containsKey(CP_HASH_STRING) != null
                        && map.containsKey(CP_HASH_NAME)
                        && map.containsKey(CP_HASH_NAME) != null
                    ) {

                        val hashData = map[CP_HASH_STRING]
                        val hashName = map[CP_HASH_NAME]

                        var hash: String? = null

                        //Below hash should be calculated only when integrating Multi-currency support. If not integrating MCP
                        // then no need to have this if check.
                        if (hashName.equals(PayUCheckoutProConstants.CP_LOOKUP_API_HASH, ignoreCase = true)){

                            //Calculate HmacSHA1 hash using the hashData and merchant secret key
                            hash = HashGenerationUtils.generateHashFromSDK(
                                hashData!!,
                                binding.etSalt.text.toString(),
                                merchantSecretKey
                            )
                        } else {
                            //calculate SDH-512 hash using hashData and salt
                            hash = HashGenerationUtils.generateHashFromSDK(
                                hashData!!,
                                binding.etSalt.text.toString()
                            )
                        }

                        if (!TextUtils.isEmpty(hash)) {
                            val hashMap: HashMap<String, String?> = HashMap()
                            hashMap[hashName!!] = hash!!
                            hashGenerationListener.onHashGenerated(hashMap)
                        }
                    }
                }

                override fun setWebViewProperties(webView: WebView?, bank: Any?) {
                }
            })
    }

    private fun getCheckoutProConfig(): PayUCheckoutProConfig {
        val checkoutProConfig = PayUCheckoutProConfig()
        checkoutProConfig.paymentModesOrder = getCheckoutOrderList()
        checkoutProConfig.offerDetails = getOfferDetailsList()
        checkoutProConfig.showCbToolbar = !binding.switchHideCbToolBar.isChecked
        checkoutProConfig.autoSelectOtp = binding.switchAutoSelectOtp.isChecked
        checkoutProConfig.autoApprove = binding.switchAutoApprove.isChecked
        checkoutProConfig.surePayCount = binding.etSurePayCount.text.toString().toInt()
        checkoutProConfig.cartDetails = reviewOrderAdapter?.getOrderDetailsList()
        checkoutProConfig.showExitConfirmationOnPaymentScreen =
            !binding.switchDiableCBDialog.isChecked
        checkoutProConfig.showExitConfirmationOnCheckoutScreen =
            !binding.switchDiableUiDialog.isChecked
        checkoutProConfig.merchantName = binding.etMerchantName.text.toString()
        checkoutProConfig.merchantLogo = R.drawable.merchant_logo
        checkoutProConfig.waitingTime = 3000
        checkoutProConfig.merchantResponseTimeout = 3000
        return checkoutProConfig
    }

    private fun getOfferDetailsList(): ArrayList<PayUOfferDetails> {
        val offerDetails = ArrayList<PayUOfferDetails>()
        offerDetails.add(PayUOfferDetails().also {
            it.offerTitle = " Instant discount of Rs.2"
            it.offerDescription = "Get Instant dicount of Rs.2 on all Credit and Debit card transactions"
            it.offerKey = "OfferKey@9227"
            it.offerPaymentTypes = ArrayList<PaymentType>().also {
                it.add(PaymentType.CARD)
            }
        })
        offerDetails.add(PayUOfferDetails().also {
            it.offerTitle = " Instant discount of Rs.2"
            it.offerDescription = "Get Instant dicount of Rs.2 on all NetBanking transactions"
            it.offerKey = "TestOffer100@9229"
            it.offerPaymentTypes = ArrayList<PaymentType>().also {
                it.add(PaymentType.NB)
            }
        })

        return offerDetails
    }

    private fun getCheckoutOrderList(): ArrayList<PaymentMode> {
        val checkoutOrderList = ArrayList<PaymentMode>()
        if (binding.switchShowGooglePay.isChecked) checkoutOrderList.add(
            PaymentMode(
                PaymentType.UPI,
                PayUCheckoutProConstants.CP_GOOGLE_PAY
            )
        )
        if (binding.switchShowPhonePe.isChecked) checkoutOrderList.add(
            PaymentMode(
                PaymentType.WALLET,
                PayUCheckoutProConstants.CP_PHONEPE
            )
        )
        if (binding.switchShowPaytm.isChecked) checkoutOrderList.add(
            PaymentMode(
                PaymentType.WALLET,
                PayUCheckoutProConstants.CP_PAYTM
            )
        )
        return checkoutOrderList
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.clMain, message, Snackbar.LENGTH_LONG).show()
    }

    private fun processResponse(response: Any) {
        response as HashMap<*, *>
        Log.d(
            BaseApiLayerConstants.SDK_TAG,
            "payuResponse ; > " + grplnk + response[PayUCheckoutProConstants.CP_PAYU_RESPONSE]
                    + ", merchantResponse : > " + response[PayUCheckoutProConstants.CP_MERCHANT_RESPONSE]
        )

        AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
            .setCancelable(false)
            .setMessage(
                "Payu's Data : " + grplnk + response.get(PayUCheckoutProConstants.CP_PAYU_RESPONSE) + "\n\n\n Merchant's Data: " + response.get(
                    PayUCheckoutProConstants.CP_MERCHANT_RESPONSE
                )
            )
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, cancelButton -> dialog.dismiss() }.show()
    }
}