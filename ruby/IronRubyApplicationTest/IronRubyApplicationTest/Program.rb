require 'Form1.rb'


System::Windows::Forms::Application.EnableVisualStyles()
System::Windows::Forms::Application.SetCompatibleTextRenderingDefault(false);
f = Form1.new
f.InitializeComponent
System::Windows::Forms::Application.Run(f)