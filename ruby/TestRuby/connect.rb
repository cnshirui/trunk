require 'win32ole'

ie = WIN32OLE.new('InternetExplorer.Application')
ie.visible = true
ie.navigate('http://www.lilybbs.net')