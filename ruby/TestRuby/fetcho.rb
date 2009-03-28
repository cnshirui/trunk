require 'win32ole'

$ie = WIN32OLE.new('PowerPoint.Application')
p $ie.ole_methods
p $ie.ole_func_methods
p $ie.ole_get_methods
p $ie.ole_put_methods
p $ie.ole_obj_help