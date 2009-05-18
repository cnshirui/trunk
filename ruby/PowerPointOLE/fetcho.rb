require 'win32ole'

$ie = WIN32OLE.new('powerpoint.application')
p $ie.class
p $ie.ole_methods
p $ie.ole_func_methods
p $ie.ole_get_methods
p $ie.ole_put_methods
p $ie.ole_obj_help

$ie2 = WIN32OLE.new('word.application')
p $ie2.class
p $ie2.ole_methods
p $ie2.ole_func_methods
p $ie2.ole_get_methods
p $ie2.ole_put_methods
p $ie2.ole_obj_help

$ie3 = WIN32OLE.new('excel.application')
p $ie3.class
p $ie3.ole_methods
p $ie3.ole_func_methods
p $ie3.ole_get_methods
p $ie3.ole_put_methods
p $ie3.ole_obj_help