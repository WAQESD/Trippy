package com.ssafy.trippy.path.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.trippy.config.S3Downloader;
import com.ssafy.trippy.config.S3Uploader;
import com.ssafy.trippy.path.model.mapper.PathMapper;
import com.ssafy.trippy.path.model.MyPageDto;
import com.ssafy.trippy.path.model.MyPageResponse;
import com.ssafy.trippy.path.model.PathDto;
import com.ssafy.trippy.path.model.WaypointDto;
import com.ssafy.trippy.path.model.WaypointRequest;

@Service
public class PathServiceImpl implements PathService {
	
	private final S3Uploader s3Uploader;
	private final S3Downloader s3Downloader;
	private final PathMapper pathMapper;

	public PathServiceImpl(S3Uploader s3Uploader, S3Downloader s3Downloader, PathMapper pathMapper) {
		super();
		this.s3Uploader = s3Uploader;
		this.s3Downloader = s3Downloader;
		this.pathMapper = pathMapper;
	}

	@Override
    @Transactional 
    public String createTeam(String file) {
        String key = "";
        //파일을 저장할 폴더, 해당 폴더 하위에 파일을 저장하고, 존재하지 않는다면 폴더를 생성하여 저장
        if(file != null) key = s3Uploader.uploadFileToS3(file, "trippyfinalpjt/path");
        return key;
    }

	@Override
	public int setPathInfo(PathDto pathDto) {
		return pathMapper.insertPathInfo(pathDto);		
	}
	
	
//	@Override
//	public void saveUserPath(String path) {
//		// TODO Auto-generated method stub
//		
//	}
	
	@Override
    @Transactional
    public List<PathDto> getPathList(String userEmail) throws IOException {
		//먼저 userEmail에 해당하는 경로 키값들을 찾아옴 
		List<PathDto> pathList = pathMapper.getUserPaths(userEmail);
		return pathList;

    }

	@Override
	public ResponseEntity<String> getPathFile(String pathId) throws IOException {
		String key = pathMapper.getFileKey(pathId);
		//pathId에 해당하는 파일 key값으로 버킷 접근, 파일 리턴
		return s3Downloader.getObject(key);
		
	}

	@Override
	public void setWaypoint(WaypointRequest waypointRequest) {
		for (WaypointDto item : waypointRequest.getWaypoints()) {
			item.setPathId(waypointRequest.getPathId());
			System.out.println("item arriTime: " + item.getArrivalTime());
			pathMapper.insertWaypoint(item);
		}
		
		
	}

	@Override
	public void deletePathFile(String key) throws Exception {
		s3Downloader.deleteS3(key);
	}

	public void deletePath(String pathId) {
		pathMapper.deletePath(pathId);
	}

	@Override
	public List<MyPageResponse> getMyPageInfo(String userEmail) {
//		join해서 가져온 데이터를 더 예쁘게 가공하기 위해 response dto에 담는과정 
		List<MyPageDto> list = pathMapper.getMyPageInfo(userEmail);
		
		List<MyPageResponse> response = new ArrayList<>();
		List<WaypointDto> wayList = new ArrayList<WaypointDto>();
		MyPageResponse mypageResponse = new MyPageResponse();
		int idChk = 0;
		
		for (MyPageDto dto : list) {
			System.out.println("dto: " + dto);
			if(dto.getPathId() != idChk) {
				if(idChk != 0) {
					mypageResponse.setList(wayList);
					response.add(mypageResponse);
				}
				
				mypageResponse = new MyPageResponse();
				
				idChk = dto.getPathId();
				
				PathDto pathDto = new PathDto();
				pathDto.setPathId(dto.getPathId());
				pathDto.setPathKey(dto.getPathKey());
				pathDto.setRegDate(dto.getRegDate());
				pathDto.setUserEmail(dto.getUserEmail());
				
				mypageResponse.setPathDto(pathDto);
				
				wayList = new ArrayList<WaypointDto>();
				WaypointDto waypoint = new WaypointDto();
				waypoint.setPathId(dto.getPathId());
				waypoint.setPlaceName(dto.getPlaceName());
				waypoint.setArrivalTime(dto.getArrivalTime());
				wayList.add(waypoint);
				
			}else {
				WaypointDto waypoint = new WaypointDto();
				waypoint.setPathId(dto.getPathId());
				waypoint.setPlaceName(dto.getPlaceName());
				waypoint.setArrivalTime(dto.getArrivalTime());
				wayList.add(waypoint);
			}
		}
		mypageResponse.setList(wayList);
		response.add(mypageResponse);
		return response;
	}
	
	
	

	
    
	
	
	

}
