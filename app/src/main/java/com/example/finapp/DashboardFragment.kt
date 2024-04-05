package com.example.finapp

import androidx.fragment.app.Fragment

class DashboardFragment : Fragment() /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Exemplo de uso de AppCompatActivity
        if (activity is AppCompatActivity) {
            val actionBar = (activity as AppCompatActivity).supportActionBar
            actionBar?.setTitle("New")
        }
    }


    // Floating Action Buttons
    private lateinit var fMainBtn: FloatingActionButton
    private lateinit var fIncomeBtn: FloatingActionButton
    private lateinit var fExpenseBtn: FloatingActionButton

    // Floating Action Button TextViews
    private lateinit var fIncomeTxt: TextView
    private lateinit var fExpenseTxt: TextView

    private var isOpen = false

    private lateinit var fadeOpen: Animation
    private lateinit var fadeClose: Animation

    //Firebase
    private lateinit var eAuth: FirebaseAuth
    private lateinit var eIncomeDatabase: DatabaseReference
    private lateinit var eExpenseDatabase: DatabaseReference


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_dashboard, container, false)

        eAuth = FirebaseAuth.getInstance()
        val currentUser = eAuth.currentUser
        val uid = currentUser?.uid


        val database = Firebase.database

        uid?.let {
            eIncomeDatabase = FirebaseDatabase.getInstance().getReference("IncomeData").child(it)
            eExpenseDatabase = FirebaseDatabase.getInstance().getReference("ExpenseData").child(it)
        }


        fMainBtn = myView.findViewById(R.id.main_plus_btn)
        fIncomeBtn = myView.findViewById(R.id.income_btn)
        fExpenseBtn = myView.findViewById(R.id.expense_btn)

        fIncomeTxt = myView.findViewById(R.id.income_text)
        fExpenseTxt = myView.findViewById(R.id.expense_text)

        fadeOpen = AnimationUtils.loadAnimation(activity, R.anim.fade_open)
        fadeClose = AnimationUtils.loadAnimation(activity, R.anim.fade_close)

        fMainBtn.setOnClickListener { view ->

            if (isOpen) {
                fIncomeBtn.startAnimation(fadeClose)
                fExpenseBtn.startAnimation(fadeClose)
                fIncomeBtn.isClickable = false
                fExpenseBtn.isClickable = false

                fIncomeTxt.startAnimation(fadeClose)
                fExpenseTxt.startAnimation(fadeClose)
                fIncomeTxt.isClickable = false
                fExpenseTxt.isClickable = false
                isOpen = false
            } else {
                addData()

                fIncomeBtn.startAnimation(fadeOpen)
                fExpenseBtn.startAnimation(fadeOpen)
                fIncomeBtn.isClickable = true
                fExpenseBtn.isClickable = true

                fIncomeTxt.startAnimation(fadeOpen)
                fExpenseTxt.startAnimation(fadeOpen)
                fIncomeTxt.isClickable = true
                fExpenseTxt.isClickable = true
                isOpen = true
            }
        }

        return myView
    }

    //Floating Button Animation
    private fun ftAnimation() {
        if (isOpen) {
            fIncomeBtn.startAnimation(fadeClose)
            fExpenseBtn.startAnimation(fadeClose)
            fIncomeBtn.isClickable = false
            fExpenseBtn.isClickable = false

            fIncomeTxt.startAnimation(fadeClose)
            fExpenseTxt.startAnimation(fadeClose)
            fIncomeTxt.isClickable = false
            fExpenseTxt.isClickable = false
            isOpen = false
        } else {
            addData()

            fIncomeBtn.startAnimation(fadeOpen)
            fExpenseBtn.startAnimation(fadeOpen)
            fIncomeBtn.isClickable = true
            fExpenseBtn.isClickable = true

            fIncomeTxt.startAnimation(fadeOpen)
            fExpenseTxt.startAnimation(fadeOpen)
            fIncomeTxt.isClickable = true
            fExpenseTxt.isClickable = true
            isOpen = true
        }
    }



    private fun addData() {
        fIncomeBtn.setOnClickListener { view ->
            incomeDataInsert()
        }

        fExpenseBtn.setOnClickListener { view ->
            expenseDataInsert()
        }
    }

    private fun incomeDataInsert() {
        val myDialog = AlertDialog.Builder(activity)
        val inflater = LayoutInflater.from(activity)
        val myView = inflater.inflate(R.layout.transaction_layout, null)
        myDialog.setView(myView)
        val dialog = myDialog.create()

        dialog.setCancelable(false)

        val editAmount: EditText = myView.findViewById(R.id.amount_edit)
        val editType: EditText = myView.findViewById(R.id.type_edit)
        val editNote: EditText = myView.findViewById(R.id.note_edit)

        val btnSave: Button = myView.findViewById(R.id.save_btn)
        val btnCancel: Button = myView.findViewById(R.id.cancel_btn)

        btnSave.setOnClickListener {
            val type = editType.text.toString().trim()
            val amount = editAmount.text.toString().trim()
            val note = editNote.text.toString().trim()

            if (type.isEmpty()) {
                editType.error = "Field Required"
                return@setOnClickListener
            }

            val amountInt = amount.toIntOrNull() ?: 0

            if (amount.isEmpty()) {
                editAmount.error = "Field Required"
                return@setOnClickListener
            }

            if (note.isEmpty()) {
                editNote.error = "Field Required"
                return@setOnClickListener
            }

            val id = eIncomeDatabase.push().key ?: ""
            val eDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

            val data = Data(amountInt, type, note, id, eDate)
            eIncomeDatabase.child(id).setValue(data)

            Toast.makeText(requireActivity(), "Data Added", Toast.LENGTH_SHORT).show()

            ftAnimation()
            dialog.dismiss()
        }

        btnCancel.setOnClickListener { view->
            ftAnimation()
            dialog.dismiss()
        }

        dialog.show()


    }

    private fun expenseDataInsert() {
        val myDialog = AlertDialog.Builder(requireActivity())
        val inflater = LayoutInflater.from(activity)
        val myView = inflater.inflate(R.layout.transaction_layout, null)
        myDialog.setView(myView)
        val dialog = myDialog.create()

        myDialog.setCancelable(false)

        val amount: EditText = myView.findViewById(R.id.amount_edit)
        val type: EditText = myView.findViewById(R.id.type_edit)
        val note: EditText = myView.findViewById(R.id.note_edit)

        val btnSave: Button = myView.findViewById(R.id.save_btn)
        val btnCancel: Button = myView.findViewById(R.id.cancel_btn)

        btnSave.setOnClickListener { view ->
            val emAmount = amount.text.toString().trim()
            val emType = type.text.toString().trim()
            val emNote = note.text.toString().trim()

            if (emAmount.isEmpty()) {
                amount.error = "Field Required"
                return@setOnClickListener
            }

            val inAmount = emAmount.toIntOrNull() ?: 0

            if (emType.isEmpty()) {
                type.error = "Field Required"
                return@setOnClickListener
            }

            if (emNote.isEmpty()) {
                note.error = "Field Required"
                return@setOnClickListener
            }

            val id = eExpenseDatabase.push().key ?: ""
            val eDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

            val data = Data(inAmount, emType, emNote, id, eDate)
            eExpenseDatabase.child(id).setValue(data)

            Toast.makeText(requireActivity(), "Data added", Toast.LENGTH_SHORT).show()

            ftAnimation()
            dialog.dismiss()
        }

        btnCancel.setOnClickListener { view ->
            ftAnimation()
            dialog.dismiss()
        }

        dialog.show()
    }

}*/



