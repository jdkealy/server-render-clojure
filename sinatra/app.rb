require 'sinatra'
require 'v8'
require "sinatra/base"
require "sinatra/reloader"
require 'sinatra/assetpack'
require 'json'

class MyApp < Sinatra::Base

  before do
    headers 'Access-Control-Allow-Origin' => '*', 
            'Access-Control-Allow-Methods' => ['OPTIONS', 'GET', 'POST']  
  end

  path = File.dirname(__FILE__) + '/../resources/public'
  set :public_folder, path
  configure :development do
    register Sinatra::Reloader
    register Sinatra::AssetPack
  end
  def load_route(route)
    @current_page = route
    cxt = V8::Context.new
    cxt.load('../lib/setup.js')
    cxt.load('../resources/public/javascripts/server-side.js')
    str = "bfaclojure.core.render_me_to_s('#{route}')"
    @html = cxt.eval(str)
  end
  

  # items
  def get_items
    [{name: 'grill piece'}, {name: 'pimping'}, {name: 'corvettes'}]
  end

  get "/items" do
    content_type :json
    get_items.to_json
  end

  get '/' do
    load_route("HOME")
    @preload = {items: get_items}.to_json 
    erb :index
  end
  # end items

  # other items
  def get_other_items
    [{name: 'gangster shit'}, {name: 'being ill'}, {name: 'wilin out'}]
  end

  get "/otheritems" do
    content_type :json
    get_other_items.to_json
  end
  
  get '/about' do
    load_route("ABOUT")
    @preload = {otheritems: get_other_items}.to_json
    erb :index
  end
  # end other items

end
