<h1>Live Stream with kotlin</h1>
<p>This is just a cobacoba project. In this project, we will simulate livestreaming using obs, nginx rtmp server, and kotlin exoplayer library.</p>
<h2>Setup</h2>
<ol>
  <li>Download nginx configuration from <a>repo</a>. I have configure it so that nginx can run RTMP server with HLS and DASH support</li>
  <li>Install and run docker</li>
  <li>Build the image of the docker file to insert nginx configuration into the nginx server hosted inside the docker registry</li>
  <li>Compile the docker image to run as container with port 1935:1935 for the RTMP server and x:x for http server</li>
  <p>TBC</p>
</ol>
