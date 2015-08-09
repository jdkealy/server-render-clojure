require 'sinatra'
require 'v8'

get '/' do
  cxt = V8::Context.new
  cxt.load('../lib/setup.js')
  cxt.load('../resources/public/javascripts/server-side.js')
  html = cxt.eval("bfa-clojure.core.layout.render_me_to_s()")
end
