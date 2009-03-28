require "open-uri"
require "win32ole"

class PptServiceController < ApplicationController
  wsdl_service_name 'PptService'
  SERVICE_TEMP_PATH = "c:\\temp" 
  
  def export_ppt(pres)
    pres_loc = download_resource(pres)
    ppt_path = deal_export(pres_loc)  
    service_release
    ppt_path
  end
  
  def download_resource(pres)
    pres_loc = pres.clone
    Dir.mkdir(SERVICE_TEMP_PATH) unless FileTest.exists?(SERVICE_TEMP_PATH)
    index_url = 0
    for i in 0..(pres_loc["slide_num"].to_i-1)
      slide_struct = pres_loc["slides"][i]
      for j in 0..(slide_struct["content_num"].to_i-1)
        content_struct = slide_struct["contents"][j]
        for k in 0..(content_struct["source_num"].to_i-1)
          url = content_struct["sources"][k] 
          index_url +=1
          data = open(url){ |f|  f.read }
          dataPath = SERVICE_TEMP_PATH + "\\" + index_url.to_s + getSourceFileExt(content_struct.type)
          open(dataPath,"wb"){|f|f.write(data)}
          updateURLByOrder(content_struct,k,dataPath)
          content_struct["sources"][k] = dataPath
        end
      end
    end
    return pres_loc
  end
  
  def deal_export(pres)
    begin 
          ppt = WIN32OLE.connect("PowerPoint.Application") 
    rescue 
          ppt = WIN32OLE.new("PowerPoint.Application") 
    end 
    
    pres = ppt.Presentations.add    
    slds = pres.slides    
    
    for i in 0..(pres["slide_num"].to_i-1)
      slide_struct = pres["slides"][i]
      slds.Add(slds.Count+1, 1)
      sld = slds.Item(slds.Count)
      make_slide(pres,sld,slide_struct)
    end
    
    Dir.mkdir("public/files") unless FileTest.exists?("public/files")
    filename = pres["title"].gsub(" ","_")
    pres.SaveAs("#{Dir.pwd}/public/files/#{filename}.ppt")
    ppt.Quit()
    return "files/#{filename}.ppt"
  end
  
  def make_slide(presentation,sld,slide_struct)
    for j in 0..(slide_struct["content_num"].to_i-1)
      content_struct = slide_struct["contents"][j]
      add_content(presentation,sld,content_struct)
    end 
  end
  
  def add_content(presentation,sld,content_struct)
    case content_struct["type"]
      when "photo"
          sld.Shapes.AddPicture(content_struct["sources"][0], true, true, 0, 0)          
          sld.Shapes(3).Left = (presentation.PageSetup.SlideWidth - sld.Shapes(3).Width)/2    
          sld.Shapes(3).Top = (presentation.PageSetup.SlideHeight - sld.Shapes(3).Height)/2
      #add other type here...
    end
  end
  
  def getSourceFileExt(content_type)
    case content_type
      when "photo"
        ".jpg"
      #add other type here...
    end
  end
  
  def service_release
    #remove temp here
  end  
  
end
