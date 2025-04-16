package planpad.planpadapp.controller.calendar;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.api.OnlyMessageResponseDto;
import planpad.planpadapp.dto.api.SaveResponseDto;
import planpad.planpadapp.dto.api.SaveResponseWrapper;
import planpad.planpadapp.dto.api.calendar.ColorPalettesResponseWrapper;
import planpad.planpadapp.dto.calendar.ColorPaletteRequestDto;
import planpad.planpadapp.dto.calendar.ColorPaletteUpdateRequestDto;
import planpad.planpadapp.dto.calendar.ColorPalettesResponseDto;
import planpad.planpadapp.service.calendar.ColorPaletteService;
import planpad.planpadapp.service.user.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Color Palette API", description = "색상 팔레트 관리 API")
public class ColorPaletteController {

    private final UserService userService;
    private final ColorPaletteService colorPaletteService;

    @PostMapping("/color-palette")
    @Operation(summary = "색상 추가", description = "색상 팔레트에 색상을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "색상 추가 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SaveResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "색상 추가 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> createColor(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid ColorPaletteRequestDto request) {

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            User user = userService.getUserByUserToken(userToken);
            Long colorId = colorPaletteService.createColor(user, request);

            return ResponseEntity.ok(new SaveResponseWrapper(new SaveResponseDto(colorId), "색상 추가에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("색상 추가에 실패하였습니다."));
        }
    }

    @GetMapping("/color-palettes")
    @Operation(summary = "색상 리스트 조회", description = "색상 팔레트의 색상 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "색상 리스트 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ColorPalettesResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "색상 리스트 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OnlyMessageResponseDto.class)))
    })
    public ResponseEntity<Object> getColors(@RequestHeader("Authorization") String bearerToken) {

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            User user = userService.getUserByUserToken(userToken);
            List<ColorPalettesResponseDto> colors = colorPaletteService.getColors(user);

            return ResponseEntity.ok(new ColorPalettesResponseWrapper(colors, "색상 리스트 조회에 성공하였습니다."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new OnlyMessageResponseDto("색상 리스트 조회에 실패하였습니다."));
        }
    }

    @PatchMapping("/color-palette/{id}")
    @Operation(summary = "색상 수정", description = "색상 팔레트의 색상을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "색상 수정 성공"),
            @ApiResponse(responseCode = "400", description = "색상 수정 실패")
    })
    public ResponseEntity<Void> updateColor(@RequestHeader("Authorization") String bearerToken, @PathVariable("id") Long id, @RequestBody @Valid ColorPaletteUpdateRequestDto request) {

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            User user = userService.getUserByUserToken(userToken);
            colorPaletteService.updateColor(user, id, request);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/color-palette/{id}")
    @Operation(summary = "색상 삭제", description = "색상 팔레트의 색상을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "색상 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "색상 삭제 실패")
    })
    public ResponseEntity<Void> deleteColor(@RequestHeader("Authorization") String bearerToken, @PathVariable("id") Long id) {

        try {
            String userToken = bearerToken.replace("Bearer ", "");
            User user = userService.getUserByUserToken(userToken);
            colorPaletteService.deleteColor(user, id);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
