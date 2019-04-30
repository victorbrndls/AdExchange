package com.harystolho.adexchange.services;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.repositories.ad.AdRepository;

@RunWith(MockitoJUnitRunner.class)
public class AdServiceTest {

	@InjectMocks
	AdService adService;

	@Mock
	AdRepository adRepository;

}
