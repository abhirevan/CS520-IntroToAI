(define (domain sokobanchallenge-domain)
	(:requirements :equality)
	(:predicates 
		(has_agent ?square)
		(has_box ?square)
		(adj ?square-1 ?square-2)
		(in_straight_line ?square-1 ?square-2)) 
  (:action move_agent
           :parameters 		(?from ?to)
           :precondition 	(and	(has_agent ?from)
									(not(has_box ?to))
									(adj ?from ?to))
           :effect		   	(and 	(has_agent ?to)
									(not (has_agent ?from))))
  (:action push_box
		   :parameters 		(?agent-pos ?box-from ?box-to)
		   :precondition 	(and 	(has_agent ?agent-pos)
									(has_box ?box-from)
									(not (has_box ?box-to))
									(adj ?agent-pos ?box-from)
									(adj ?box-from ?box-to)
									(in_straight_line ?agent-pos ?box-to))
	       :effect 			(and 	(has_agent ?box-from)
									(not(has_agent ?agent-pos))
									(has_box ?box-to)
									(not(has_box ?box-from))))
  )
