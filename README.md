# image-processing-algos
Java implementation of image processing algrithms, packaged as ImageJ plugins.

## Canny Edge Detection ##
1. Compute x and y gradient respectively. (Sobel operator might be used) 
2. Compute gradient magnitude and direction :
	
		grad_magnitude[u, v] = sqrt(gradient_x[u, v]^2 + gradient_y[u, v]^2)
		grad_angle[u, v] = atan(gradient_y[u, v] + gradient_x[u, v])

3. Non-maximum suppression : divide the 8-neighborhoods of gradient magnitude at [u,v] into 4 regions, and decide which 2 of 8 neighbors is actually adjacent to gradient\_mag[u, v], according to the gradient direction. If gradient\_mag[u,v] is samller than one or both of its 2 adjacent neighbors, it get suppressed (set to zero).

<div align=center>
<img src="/img/figures/discrete_gradient_directions.png" width="30%">
<br/>
Discrete Gradient Directions

</div>
	
4. Hysteresis Threshold : Two threshold values, T_low and T_high are used. 
	
		boolean[][] edges = new boolean[width][height];
		
		if (gradient_mag[u,v] > T_high) edges[u][v] == true;
		else (gradient_mag[u, v] > T_low && one of its 8 neighbors are maked true in edges) edges[u][v] = true;
		
<br/>
<br/>


<div align=center>
<img src="/img/screenshots/canny_edge.png" width="60%">
<br/>	
Intermediate and Final Outputs
	
</div>

