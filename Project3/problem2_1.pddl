(define (problem sokoban-1)
	(:domain sokobanchallenge-domain)
	(:objects sq_1_1 sq_1_2 sq_2_2 sq_3_1 sq_3_2 sq_3_3 sq_3_4 sq_4_1 sq_4_2 sq_4_3 sq_4_4 sq_5_1 sq_5_2)
	(:init
		(adj sq_1_1 sq_1_2) (adj sq_1_2 sq_1_1)
		
		(adj sq_1_2 sq_2_2) (adj sq_2_2 sq_1_2)
		
		(adj sq_3_1 sq_3_2) (adj sq_3_2 sq_3_1)
		(adj sq_3_2 sq_3_3) (adj sq_3_3 sq_3_2)
		(adj sq_3_3 sq_3_4) (adj sq_3_4 sq_3_3)
		
		(adj sq_2_2 sq_3_2) (adj sq_3_2 sq_2_2)
		
		(adj sq_4_1 sq_4_2) (adj sq_4_2 sq_4_1)
		(adj sq_4_2 sq_4_3) (adj sq_4_3 sq_4_2)
		(adj sq_4_3 sq_4_4) (adj sq_4_4 sq_4_3)
		
		(adj sq_3_1 sq_4_1) (adj sq_4_1 sq_3_1)
		(adj sq_3_2 sq_4_2) (adj sq_4_2 sq_3_2)
		(adj sq_3_3 sq_4_3) (adj sq_4_3 sq_3_3)
		(adj sq_3_4 sq_4_4) (adj sq_4_4 sq_3_4)
		
		(adj sq_5_1 sq_5_2) (adj sq_5_2 sq_5_1)
		
		(adj sq_4_1 sq_5_1) (adj sq_5_1 sq_4_1)
		(adj sq_4_2 sq_5_2) (adj sq_5_2 sq_4_2)
		
		;;(has_agent sq_2_1)
		;;(has_box sq_2_2)
		
		(has_agent sq_2_2)
		(has_box sq_4_3)
		
		(in_straight_line sq_1_2 sq_3_2) (in_straight_line sq_3_2 sq_1_2)
		(in_straight_line sq_3_1 sq_3_3) (in_straight_line sq_3_3 sq_3_1)
		(in_straight_line sq_3_2 sq_3_4) (in_straight_line sq_3_4 sq_3_2)
		(in_straight_line sq_4_1 sq_4_3) (in_straight_line sq_4_3 sq_4_1)
		(in_straight_line sq_4_2 sq_4_4) (in_straight_line sq_4_4 sq_4_2)
		(in_straight_line sq_3_1 sq_5_1) (in_straight_line sq_5_1 sq_3_1)
		(in_straight_line sq_3_2 sq_5_2) (in_straight_line sq_5_2 sq_3_2)
		
		(in_straight_line sq_2_2 sq_4_2) (in_straight_line sq_4_2 sq_2_2))
     
	(:goal (and (has_box sq_1_2)))
)
