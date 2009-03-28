class PptServiceApi < ActionWebService::API::Base
  api_method :export_ppt,
             :expects => [Presentation],
             :returns => [:string]
end
