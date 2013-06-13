This is an example of connecting an Android App with a Rails App using an HTTPClient. 

It is based of this tutorial:
http://mgmblog.com/2008/10/01/android-ruby-on-rails-app-setup-and-httpclient-hello-world/

Since the original blog post was created 5 years ago, I found that I needed to make a lot of changes.  In particular, I had to move the HTTP request to an AsyncTask since they are no longer allowed in the main thread.

I also needed to edit the Controller for my rails app to include the following lines in my index action:

  def index
    @projects = Project.all

    respond_to do |format|
      format.html # index.html.erb
      format.xml {render :xml=> @projects}
      format.json { render json: @projects }
    end
  end

Remember to add the following line to your manifest file:

" uses-permission android:name="android.permission.INTERNET" "