require 'sinatra'
require 'v8'
require "sinatra/base"
require "sinatra/reloader"
require 'sinatra/assetpack'

class MyApp < Sinatra::Base
  path = File.dirname(__FILE__) + '/../resources/public'
  set :public_folder, path
  configure :development do
    register Sinatra::Reloader
    register Sinatra::AssetPack
  end
  def load_route(route)
    cxt = V8::Context.new
    cxt.load('../lib/setup.js')
    cxt.load('../resources/public/javascripts/server-side.js')
    str = "bfaclojure.core.render_me_to_s('#{route}')"
    @html = cxt.eval(str)
  end
  
  get '/' do
    load_route("HOME")
    erb :index
  end
  
  get '/about' do
    load_route("ABOUT")
    erb :index
  end
end
