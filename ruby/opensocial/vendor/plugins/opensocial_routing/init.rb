require 'routing'

Mime::Type.register "application/x-opensocial", :opensocial
ActionController::Routing::RouteSet::Mapper.send :include, OpenSocial::Routing