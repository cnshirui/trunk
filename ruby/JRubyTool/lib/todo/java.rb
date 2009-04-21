require 'java'
include_class "javax.swing.JFrame"
include_class "javax.swing.JLabel"
frame = JFrame.new()
jlabel = JLabel.new("Hello from JRuby with Swing")
frame.getContentPane().add(jlabel)
# frame.content_pane.add(label)
frame.pack()
frame.setVisible(true)
frame.visible = true