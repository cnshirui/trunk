#
# IronRuby Form designer generated code - do not edit this file
#

class Object
  def cast(arg); self end
  def attr_accessor(symbol); end
end
require 'mscorlib'
require 'System, Version=2.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089, processorArchitecture=MSIL'
require 'System.Data, Version=2.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089, processorArchitecture=x86'
require 'System.Deployment, Version=2.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a, processorArchitecture=MSIL'
require 'System.Drawing, Version=2.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a, processorArchitecture=MSIL'
require 'System.Windows.Forms, Version=2.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089, processorArchitecture=MSIL'
require 'System.Xml, Version=2.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089, processorArchitecture=MSIL'

class Form1 < System::Windows::Forms::Form
  
  attr_accessor :components
  def components; @components end
  def components=(v); @components = v end
  
  def InitializeComponent
    
    self.SuspendLayout()
    # 
    # Form1
    # 
    self.ClientSize = System::Drawing::Size.new(600, 400)
    self.Name = 'Form1'
    self.Load do |sender, e| Form1_Load(sender, e) end
    self.ResumeLayout(false)
    end
  end
