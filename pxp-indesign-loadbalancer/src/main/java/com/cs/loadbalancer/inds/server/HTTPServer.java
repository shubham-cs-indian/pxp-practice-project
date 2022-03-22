package com.cs.loadbalancer.inds.server;

import java.io.IOException;
import java.net.BindException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.cs.loadbalancer.inds.scheduler.IINDSScheduler;
import com.cs.loadbalancer.inds.scheduler.INDSScheduler;
import com.cs.loadbalancer.inds.task.base.IINDSTask;
import com.cs.loadbalancer.inds.task.base.TaskFactory;
import com.cs.loadbalancer.inds.utils.INDSLoadBalancerUtils;

/**
 * The HTTPServer class, contains the main method. Reads the port no. from the properties file & starts the HTTP server.
 * Whenever, the server receives a new request, it creates a task for it & submits it to the scheduler.
 */
public class HTTPServer extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final IINDSScheduler scheduler = new INDSScheduler();

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Server", "CS InDesign Server Load Balancer 1.0");
		
		/**
		 * Its very important to use the async context since we'll wait for the task to complete & only then send back the response.
		 * Refer async context in java for more details.
		**/
		AsyncContext asyncContext = request.startAsync(request, response);
		asyncContext.setTimeout(0);
		
		
		IINDSTask task = null;
		
		try {
			task = TaskFactory.getTaskByRequestType(asyncContext, scheduler, response);
		} catch (Exception ex) {
			INDSLoadBalancerUtils.sendErrorResponse(asyncContext, ex);
			return;
		}
		
		scheduler.addTaskToScheduler(task);
	}

	public static void main(String[] args) {
		try {
			ServletContextHandler handler = new ServletContextHandler();
			ServletHolder holder = handler.addServlet(HTTPServer.class, "/");
			holder.setAsyncSupported(true); //Note: It is important to set this flag to 'true' to let the http container know that async processing needs to be supported.

      String port = "";
      if (args.length == 2) {
        if (args[0].equals("-port") && INDSLoadBalancerUtils.isNumeric(args[1])) {
          port = args[1];
        }
        else {
          System.out.println("Please add port number following -port after the command. (eg. '-port 8000')");
          System.exit(1);
        }
      }
      else {
        System.out.println("Please add port number following -port after the command. (eg. '-port 8000')");
        System.exit(1);
      }
      Server server = new Server(Integer.parseInt(port));
      server.setHandler(handler);
      server.start();
      
      System.out.println("HTTP server started at port: " + port);
			
			server.join();
		} 
		catch (BindException e) {
      System.out.println("Provided port number " + args[0] + " already in use!");
      System.exit(1);
    }
		catch (Throwable e) {
			System.out.println("Error occurred while starting HTTP server !");
			e.printStackTrace();
		}
	}

}