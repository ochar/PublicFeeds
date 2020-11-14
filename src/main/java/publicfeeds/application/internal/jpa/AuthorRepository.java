/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.application.internal.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import publicfeeds.domain.Author;

/**
 *
 * @author io
 */
public interface AuthorRepository extends JpaRepository<Author, String> {
	
}
